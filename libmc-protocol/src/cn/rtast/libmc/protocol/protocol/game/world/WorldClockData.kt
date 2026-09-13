/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.world

public data class WorldClockData(
    val clockId: Int,
    val time: Long,
    val fractionalTime: Float,
    val rate: Float,
)