/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game.player

public enum class Hand(public val id: Int) {
    MAIN_HAND(0),
    OFF_HAND(1);

    public companion object {
        public fun fromID(id: Int): Hand = entries.first { it.id == id }
    }
}