/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.protocol.game.team

public enum class CollisionRule(public val id: Int) {
    ALWAYS(0),
    NEVER(1),
    PUSH_OTHER_TEAMS(2),
    PUSH_OWN_TEAM(3);

    public companion object {
        public fun fromID(id: Int): CollisionRule = entries.firstOrNull { it.id == id } ?: ALWAYS
    }
}