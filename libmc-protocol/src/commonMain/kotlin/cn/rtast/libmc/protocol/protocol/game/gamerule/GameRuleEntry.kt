/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.gamerule

import kotlinx.serialization.Serializable

@Serializable
public data class GameRuleEntry(val name: String, val value: String)