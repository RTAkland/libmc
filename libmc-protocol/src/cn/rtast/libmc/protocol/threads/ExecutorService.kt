/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/13
 */


package cn.rtast.libmc.protocol.threads

internal expect class ExecutorService(threadCount: Int) {
    fun <T> submit(block: Future<T>.() -> T): Future<T>
    fun shutdown()
    fun shutdownNow()
}

internal fun createFixedThreadPool(threads: Int = 4): ExecutorService = ExecutorService(threads)

internal expect fun spinWait()