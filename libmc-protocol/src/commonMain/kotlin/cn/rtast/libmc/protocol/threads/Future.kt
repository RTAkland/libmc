/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/13
 */


package cn.rtast.libmc.protocol.threads

internal interface Future<T> {
    val isDone: Boolean
    val isCancelled: Boolean
    val isActive: Boolean get() = !isDone && !isCancelled

    fun cancel(mayInterruptIfRunning: Boolean = true): Boolean
    fun get(): T
}