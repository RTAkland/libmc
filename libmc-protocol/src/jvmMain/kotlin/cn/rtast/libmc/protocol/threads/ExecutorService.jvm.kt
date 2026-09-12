/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/13
 */

package cn.rtast.libmc.protocol.threads

import java.util.concurrent.Callable
import java.util.concurrent.Executors as JavaExecutors
import java.util.concurrent.Future as JavaFuture

internal class JvmFuture<T> : Future<T> {
    @Volatile
    private var delegate: JavaFuture<T>? = null

    internal fun bind(future: JavaFuture<T>) {
        this.delegate = future
    }

    override val isDone: Boolean get() = delegate?.isDone ?: false
    override val isCancelled: Boolean get() = delegate?.isCancelled ?: false
    override fun cancel(mayInterruptIfRunning: Boolean): Boolean = delegate?.cancel(mayInterruptIfRunning) ?: false
    override fun get(): T = delegate?.get() ?: throw IllegalStateException("Future delegate not initialized")
}

internal actual class ExecutorService actual constructor(private val threadCount: Int) {
    private val delegate = JavaExecutors.newFixedThreadPool(threadCount)

    actual fun <T> submit(block: Future<T>.() -> T): Future<T> {
        val jvmFuture = JvmFuture<T>()
        val javaFuture = delegate.submit(Callable { jvmFuture.block() })
        jvmFuture.bind(javaFuture)
        return jvmFuture
    }

    actual fun shutdown() = delegate.shutdown()
    actual fun shutdownNow(): Unit = run { delegate.shutdownNow() }
}

internal actual fun spinWait() = Thread.yield()