/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.data.component.attributes

public enum class SwingAnimationType(public val id: Int) {
    NONE(0), WHACK(1), STAB(2);

    public companion object {
        public fun fromID(id: Int): SwingAnimationType = entries.first { it.id == id }
    }
}