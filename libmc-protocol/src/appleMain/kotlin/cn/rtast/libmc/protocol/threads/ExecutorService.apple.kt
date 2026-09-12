/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/13
 */

@file:OptIn(ExperimentalForeignApi::class)

package cn.rtast.libmc.protocol.threads

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.staticCFunction
import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT
import platform.darwin.dispatch_async_f
import platform.darwin.dispatch_get_global_queue

internal actual class ExecutorService actual constructor(private val threadCount: Int) {
    actual fun <T> submit(block: Future<T>.() -> T): Future<T> {
        val future = NativeFuture(block)
        val stableRef = StableRef.create(future)
        dispatch_async_f(
            dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(), 0u),
            stableRef.asCPointer(),
            staticCFunction { arg ->
                val ref = arg?.asStableRef<NativeFuture<T>>()
                ref?.get()?.run()
                ref?.dispose()
            }
        )
        return future
    }

    actual fun shutdown() {}
    actual fun shutdownNow() {}
}