/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/13
 */
package cn.rtast.libmc.protocol.threads.coroutines

import java.util.concurrent.locks.ReentrantLock

internal actual class Lock : AutoCloseable {
    private val delegate = ReentrantLock()
    actual fun lock(): Unit = delegate.lock()
    actual fun unlock(): Unit = delegate.unlock()
    actual override fun close() {}
}