/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game.advancement

public enum class AdvancementAction(public val id: Int) {
    OPENED_TAB(0),
    CLOSED_SCREEN(1);

    public companion object {
        public fun fromID(id: Int): AdvancementAction = entries.first { it.id == id }
    }
}