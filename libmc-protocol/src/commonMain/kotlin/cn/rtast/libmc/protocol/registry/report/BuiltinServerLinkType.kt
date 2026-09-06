/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.registry.report

public enum class BuiltinServerLinkType(public val id: Int) {
    BUG_REPORT(0),
    COMMUNITY_GUIDELINES(1),
    SUPPORT(2),
    STATUS(3),
    FEEDBACK(4),
    COMMUNITY(5),
    WEBSITE(6),
    FORUMS(7),
    NEWS(8),
    ANNOUNCEMENTS(9);

    public companion object {
        public fun fromID(id: Int): BuiltinServerLinkType = entries.first { it.id == id }
    }
}