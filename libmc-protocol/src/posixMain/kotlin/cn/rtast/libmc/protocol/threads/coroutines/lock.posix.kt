/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/13
 */

@file:OptIn(ExperimentalForeignApi::class)

package cn.rtast.libmc.protocol.threads.coroutines

import kotlinx.cinterop.*
import platform.posix.*

internal actual class Lock actual constructor() : AutoCloseable {
    private val mutexPtr: CPointer<pthread_mutex_t> = nativeHeap.alloc<pthread_mutex_t>().ptr

    init {
        pthread_mutex_init(mutexPtr, null)
    }

    actual fun lock(): Unit = run { pthread_mutex_lock(mutexPtr) }
    actual fun unlock(): Unit = run { pthread_mutex_unlock(mutexPtr) }
    actual override fun close() {
        pthread_mutex_destroy(mutexPtr)
        nativeHeap.free(mutexPtr)
    }
}