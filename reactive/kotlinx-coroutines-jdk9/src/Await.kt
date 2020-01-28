/*
 * Copyright 2016-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.coroutines.jdk9

import java.util.concurrent.Flow.Publisher
import org.reactivestreams.FlowAdapters
import kotlinx.coroutines.reactive.*

/**
 * A thin wrapper around [kotlinx.coroutines.reactive.awaitFirst] for JDK 9 Flow [Publisher].
 */
public suspend fun <T> Publisher<T>.awaitFirst(): T = FlowAdapters.toPublisher(this).awaitFirst()

/**
 * A thin wrapper around [kotlinx.coroutines.reactive.awaitFirstOrDefault] for JDK 9 Flow [Publisher].
 */
public suspend fun <T> Publisher<T>.awaitFirstOrDefault(default: T): T =
        FlowAdapters.toPublisher(this).awaitFirstOrDefault(default)

/**
 * A thin wrapper around [kotlinx.coroutines.reactive.awaitFirstOrNull] for JDK 9 Flow [Publisher].
 */
public suspend fun <T> Publisher<T>.awaitFirstOrNull(): T? =
        FlowAdapters.toPublisher(this).awaitFirstOrNull()

/**
 * A thin wrapper around [kotlinx.coroutines.reactive.awaitFirstOrElse] for JDK 9 Flow [Publisher].
 */
public suspend fun <T> Publisher<T>.awaitFirstOrElse(defaultValue: () -> T): T =
        FlowAdapters.toPublisher(this).awaitFirstOrElse(defaultValue)

/**
 * A thin wrapper around [kotlinx.coroutines.reactive.awaitLast] for JDK 9 Flow [Publisher].
 */
public suspend fun <T> Publisher<T>.awaitLast(): T =
        FlowAdapters.toPublisher(this).awaitLast()

/**
 * A thin wrapper around [kotlinx.coroutines.reactive.awaitSingle] for JDK 9 Flow [Publisher].
 */
public suspend fun <T> Publisher<T>.awaitSingle(): T =
        FlowAdapters.toPublisher(this).awaitSingle()
