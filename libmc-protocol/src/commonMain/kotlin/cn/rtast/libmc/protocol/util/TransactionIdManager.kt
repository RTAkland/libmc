/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.util

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

public class TransactionIdManager internal constructor() {
    private var queryTransactionCounter: Int = 1
    private var commandSuggestionTransactionCounter: Int = 1
    private var queryEntityTagCounter: Int = 1
    private val mutex = Mutex()

    public suspend fun nextQueryId(): Int = mutex.withLock { queryTransactionCounter++ }
    public suspend fun nextCommandSuggestionId(): Int =
        mutex.withLock { commandSuggestionTransactionCounter++ }

    public suspend fun nextQueryEntityTagId(): Int = mutex.withLock { queryEntityTagCounter++ }
}