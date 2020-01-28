/*
 * Copyright 2016-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.coroutines.jdk9

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import java.util.concurrent.Flow.*
import kotlin.coroutines.*
import org.reactivestreams.FlowAdapters

/**
 * A thin wrapper around [kotlinx.coroutines.reactive.publish] that converts the resulting publisher into
 * a JDK 9 Flow [Publisher].
 */
@ExperimentalCoroutinesApi
public fun <T> publish(
    context: CoroutineContext = EmptyCoroutineContext,
    @BuilderInference block: suspend ProducerScope<T>.() -> Unit
): Publisher<T> {
    val reactivePublisher : org.reactivestreams.Publisher<T> = kotlinx.coroutines.reactive.publish<T>(context, block)
    return FlowAdapters.toFlowPublisher(reactivePublisher)
}
