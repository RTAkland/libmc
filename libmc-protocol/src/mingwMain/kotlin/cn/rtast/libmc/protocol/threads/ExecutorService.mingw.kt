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
import platform.windows.QueueUserWorkItem
import platform.windows.Sleep
import platform.windows.WT_EXECUTELONGFUNCTION

internal actual class ExecutorService actual constructor(private val threadCount: Int) {
    actual fun <T> submit(block: Future<T>.() -> T): Future<T> {
        val future = NativeFuture(block)
        val stableRef = StableRef.create(future)
        QueueUserWorkItem(
            staticCFunction { param ->
                val ref = param?.asStableRef<NativeFuture<T>>()
                val targetFuture = ref?.get()
                targetFuture?.run()
                ref?.dispose()
                0u
            },
            stableRef.asCPointer(),
            WT_EXECUTELONGFUNCTION.toUInt()
        )
        return future
    }

    actual fun shutdown() {}
    actual fun shutdownNow() {}
}

internal actual fun spinWait() = Sleep(0u)