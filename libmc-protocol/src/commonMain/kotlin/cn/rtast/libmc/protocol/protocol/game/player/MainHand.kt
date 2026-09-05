/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.player

public enum class MainHand(public val id: Int) {
    Left(0),
    Right(1);

    public companion object {
        public fun fromID(id: Int): MainHand = entries.first { it.id == id }
    }
}