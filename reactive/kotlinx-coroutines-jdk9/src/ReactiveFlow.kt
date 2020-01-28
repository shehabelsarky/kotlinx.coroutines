/*
 * Copyright 2016-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.coroutines.jdk9

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.asPublisher
import kotlinx.coroutines.reactive.collect
import java.util.concurrent.Flow.*
import org.reactivestreams.FlowAdapters

/**
 * A thin wrapper around [kotlinx.coroutines.reactive.asFlow] for JDK 9 Flow [Publisher].
 */
public fun <T : Any> Publisher<T>.asFlow(): Flow<T> =
        FlowAdapters.toPublisher(this).asFlow()

/**
 * A thin wrapper around [kotlinx.coroutines.reactive.asPublisher] that converts the result into
 * a JDK 9 Flow [Publisher].
 */
public fun <T : Any> Flow<T>.asPublisher(): Publisher<T> {
    val reactivePublisher : org.reactivestreams.Publisher<T> = this.asPublisher<T>()
    return FlowAdapters.toFlowPublisher(reactivePublisher)
}

/**
 * A thin wrapper around [kotlinx.coroutines.reactive.collect] for JDK 9 Flow [Publisher].
 */
public suspend fun <T> Publisher<T>.collect(action: (T) -> Unit) =
    FlowAdapters.toPublisher(this).collect(action)
