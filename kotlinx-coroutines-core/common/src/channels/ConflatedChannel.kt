package kotlinx.coroutines.channels

import kotlinx.coroutines.*
import kotlinx.coroutines.internal.*
import kotlinx.coroutines.selects.*

/**
 * Channel that buffers at most one element and conflates all subsequent `send` and `offer` invocations,
 * so that the receiver always gets the most recently sent element.
 * Back-to-send sent elements are _conflated_ -- only the the most recently sent element is received,
 * while previously sent elements **are lost**.
 * Sender to this channel never suspends and [offer] always returns `true`.
 *
 * This channel is created by `Channel(Channel.CONFLATED)` factory function invocation.
 */
internal open class ConflatedChannel<E> : AbstractChannel<E>() {
    protected final override val isBufferAlwaysEmpty: Boolean = false
    protected final override val isBufferEmpty: Boolean get() = lock.withLock { size == 0 }
    protected final override val isBufferAlwaysFull: Boolean = false
    protected final override val isBufferFull: Boolean get() = lock.withLock { size == 1 }

    protected final override val isFullImpl: Boolean = false

    val lock = ReentrantLock()

    var buffer: E? = null

    var size = 0

    // result is `OFFER_SUCCESS | Closed`
    protected override fun offerInternal(element: E): Any {
        var receive: ReceiveOrClosed<E>? = null
        lock.withLock {
            closedForSend?.let { return it }
            // if there is no element written in buffer
            if (size == 0) {
                // check for receivers that were waiting on the empty buffer
                loop@ while(true) {
                    receive = takeFirstReceiveOrPeekClosed() ?: break@loop // break when no receivers queued
                    if (receive is Closed) {
                        return receive!!
                    }
                    val token = receive!!.tryResumeReceive(element, null)
                    if (token != null) {
                        assert { token === RESUME_TOKEN }
                        return@withLock
                    }
                }
            }
            size = 1
            buffer = element
            return OFFER_SUCCESS
        }
        // breaks here if offer meets receiver
        receive!!.completeResumeReceive(element)
        return receive!!.offerResult
    }

    // result is `ALREADY_SELECTED | OFFER_SUCCESS | Closed`
    protected override fun offerSelectInternal(element: E, select: SelectInstance<*>): Any {
        var receive: ReceiveOrClosed<E>? = null
        lock.withLock {
            closedForSend?.let { return it }
            if (size == 0) {
                loop@ while (true) {
                    val offerOp = describeTryOffer(element)
                    val failure = select.performAtomicTrySelect(offerOp)
                    when {
                        failure == null -> { // offered successfully
                            receive = offerOp.result
                            return@withLock
                        }
                        failure === OFFER_FAILED -> break@loop // cannot offer -> Ok to queue to buffer
                        failure === RETRY_ATOMIC -> {} // retry
                        failure === ALREADY_SELECTED || failure is Closed<*> -> return failure
                        else -> error("performAtomicTrySelect(describeTryOffer) returned $failure")
                    }
                }
            }
            // try to select sending this element to buffer
            if (!select.trySelect()) {
                return ALREADY_SELECTED
            }
            size = 1
            buffer = element
            return OFFER_SUCCESS
        }
        // breaks here if offer meets receiver
        receive!!.completeResumeReceive(element)
        return receive!!.offerResult
    }

    // result is `E | POLL_FAILED | Closed`
    protected override fun pollInternal(): Any? {
        var result: Any? = null
        lock.withLock {
            if (size == 0) return closedForSend ?: POLL_FAILED
            result = buffer
            buffer = null
            size = 0
        }
        return result
    }

    // result is `E | POLL_FAILED | Closed`
    protected override fun pollSelectInternal(select: SelectInstance<*>): Any? {
        var result: Any? = null
        lock.withLock {
            if (size == 0) return closedForSend ?: POLL_FAILED
            if (!select.trySelect())
                return ALREADY_SELECTED
            result = buffer
            buffer = null
            size = 0
        }
        return result
    }

    protected override fun onCancelIdempotent(wasClosed: Boolean) {
        if (wasClosed) {
            lock.withLock {
                buffer = null
                size = 0
            }
        }
    }
}