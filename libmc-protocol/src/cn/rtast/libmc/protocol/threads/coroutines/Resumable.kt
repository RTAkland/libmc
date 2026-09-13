/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/13
 */


package cn.rtast.libmc.protocol.threads.coroutines

import cn.rtast.libmc.protocol.threads.spinWait

internal interface Resumable<in T> {
    fun resume(result: T)
    fun resumeWithException(e: Throwable)
}

internal fun <T> runAsCompletable(block: (resumable: Resumable<T>) -> Unit): T {
    var resultHolder: T? = null
    var exceptionHolder: Throwable? = null
    var isCompleted = false
    val completer = object : Resumable<T> {
        override fun resume(result: T) {
            if (isCompleted) return
            resultHolder = result
            isCompleted = true
        }

        override fun resumeWithException(e: Throwable) {
            if (isCompleted) return
            exceptionHolder = e
            isCompleted = true
        }
    }
    block(completer)
    while (!isCompleted) spinWait()
    exceptionHolder?.let { throw it }
    @Suppress("UNCHECKED_CAST")
    return resultHolder as T
}