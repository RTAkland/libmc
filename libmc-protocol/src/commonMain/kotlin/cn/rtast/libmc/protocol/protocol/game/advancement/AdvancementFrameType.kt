/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.protocol.game.advancement

public enum class AdvancementFrameType(public val id: Int) {
    TASK(0), CHALLENGE(1), GOAL(2);

    public companion object {
        public fun fromID(id: Int): AdvancementFrameType = entries.first { it.id == id }
    }
}