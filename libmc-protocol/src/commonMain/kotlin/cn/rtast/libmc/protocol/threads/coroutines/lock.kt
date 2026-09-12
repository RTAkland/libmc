/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/13
 */


package cn.rtast.libmc.protocol.threads.coroutines

internal expect class Lock() : AutoCloseable {
    fun lock()
    fun unlock()
    override fun close()
}

internal inline fun <T> Lock.withLock(action: () -> T): T {
    lock()
    try {
        return action()
    } finally {
        unlock()
    }
}