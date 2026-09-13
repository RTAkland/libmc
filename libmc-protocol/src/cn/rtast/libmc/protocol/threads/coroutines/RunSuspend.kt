/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/13
 */


package cn.rtast.libmc.protocol.threads.coroutines

import cn.rtast.libmc.protocol.threads.spinWait
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

/**
 * A function to create an empty coroutine context,
 * same as `runBlocking` in `kotlinx-coroutines`
 */
public fun <T> runSuspend(block: suspend () -> T): T {
    var resultHolder: Result<T>? = null
    val continuation = object : Continuation<T> {
        override val context: CoroutineContext = EmptyCoroutineContext
        override fun resumeWith(result: Result<T>) {
            resultHolder = result
        }
    }
    block.startCoroutine(continuation)
    while (resultHolder == null) spinWait()
    return resultHolder.getOrThrow()
}