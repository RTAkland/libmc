/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.util

import cn.rtast.libmc.protocol.threads.coroutines.Lock
import cn.rtast.libmc.protocol.threads.coroutines.withLock

/**
 * Client-side managed transaction id manager,
 * managed an auto-increment transaction id
 */
public class TransactionIdManager internal constructor() {
    private var queryTransactionCounter: Int = 1
    private var commandSuggestionTransactionCounter: Int = 1
    private var queryEntityTagCounter: Int = 1
    private val lock = Lock()

    public fun nextQueryId(): Int = lock.withLock { queryTransactionCounter++ }
    public fun nextCommandSuggestionId(): Int =
        lock.withLock { commandSuggestionTransactionCounter++ }

    public fun nextQueryEntityTagId(): Int = lock.withLock { queryEntityTagCounter++ }
}