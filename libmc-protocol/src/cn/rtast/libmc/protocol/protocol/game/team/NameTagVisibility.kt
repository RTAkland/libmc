/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.protocol.game.team

public enum class NameTagVisibility(public val id: Int) {
    ALWAYS(0),
    NEVER(1),
    HIDE_FOR_OTHER_TEAMS(2),
    HIDE_FOR_OWN_TEAMS(3);

    public companion object {
        public fun fromID(id: Int): NameTagVisibility = entries.first { it.id == id }
    }
}