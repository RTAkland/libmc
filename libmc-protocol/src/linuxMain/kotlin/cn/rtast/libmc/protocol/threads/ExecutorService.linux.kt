/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/13
 */

@file:OptIn(ExperimentalForeignApi::class)

package cn.rtast.libmc.protocol.threads

import kotlinx.cinterop.*
import platform.posix.pthread_create
import platform.posix.pthread_detach
import platform.posix.pthread_tVar

internal actual class ExecutorService actual constructor(private val threadCount: Int) {
    actual fun <T> submit(block: Future<T>.() -> T): Future<T> {
        val future = NativeFuture(block)
        val stableRef = StableRef.create(future)
        memScoped {
            val threadId = alloc<pthread_tVar>()
            val result = pthread_create(
                threadId.ptr,
                null,
                staticCFunction { arg ->
                    val ref = arg?.asStableRef<NativeFuture<T>>()
                    val targetFuture = ref?.get()
                    targetFuture?.run()
                    ref?.dispose()
                    null
                },
                stableRef.asCPointer()
            )
            if (result != 0) {
                stableRef.dispose()
                throw IllegalStateException("Failed to create POSIX thread, errno: $result")
            }
            pthread_detach(threadId.value)
        }
        return future
    }

    actual fun shutdown() {}
    actual fun shutdownNow() {}
}