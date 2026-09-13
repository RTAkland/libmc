/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/13
 */

@file:OptIn(ExperimentalForeignApi::class)

package cn.rtast.libmc.protocol.threads.coroutines

import kotlinx.cinterop.*
import platform.windows.*

public actual class Lock actual constructor() : AutoCloseable {
    private val criticalSectionPtr: CPointer<CRITICAL_SECTION> = nativeHeap.alloc<CRITICAL_SECTION>().ptr

    init {
        InitializeCriticalSection(criticalSectionPtr)
    }

    public actual fun lock() {
        EnterCriticalSection(criticalSectionPtr)
    }

    public actual fun unlock() {
        LeaveCriticalSection(criticalSectionPtr)
    }

    public actual override fun close() {
        DeleteCriticalSection(criticalSectionPtr)
        nativeHeap.free(criticalSectionPtr)
    }
}