/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/13
 */

package cn.rtast.libmc.protocol.threads

import cn.rtast.libmc.protocol.threads.coroutines.Lock
import cn.rtast.libmc.protocol.threads.coroutines.withLock
import kotlin.concurrent.Volatile

internal class NativeFuture<T>(private val task: Future<T>.() -> T) : Future<T> {
    private val lock = Lock()

    @Volatile
    override var isDone: Boolean = false
        private set

    @Volatile
    override var isCancelled: Boolean = false
        private set

    private var value: T? = null
    private var exception: Throwable? = null

    internal fun run() {
        val shouldRun = lock.withLock { !isCancelled }
        if (!shouldRun) return
        try {
            val result = this.task()
            lock.withLock {
                if (!isCancelled) {
                    value = result
                    isDone = true
                }
            }
        } catch (e: Throwable) {
            lock.withLock {
                if (!isCancelled) {
                    exception = e
                    isDone = true
                }
            }
        }
    }

    override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
        return lock.withLock {
            if (isDone || isCancelled) return@withLock false
            isCancelled = true
            isDone = true
            true
        }
    }

    override fun get(): T {
        while (!isDone && !isCancelled) spinWait()
        return lock.withLock {
            if (isCancelled) {
                throw IllegalStateException("Task was cancelled")
            }
            exception?.let { throw it }
            @Suppress("UNCHECKED_CAST")
            value as T
        }
    }
}