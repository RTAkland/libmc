/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.chat.util

import kotlin.uuid.Uuid


public fun generateOfflineUuid(username: String): Uuid =
    Uuid.fromByteArray("OfflinePlayer:$username".encodeToByteArray())