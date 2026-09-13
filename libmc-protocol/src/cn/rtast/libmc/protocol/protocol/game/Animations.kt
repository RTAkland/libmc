/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Entity_Animation
 */
public enum class Animations(public val animationId: Byte) {
    SwingMainArm(0),
    LeaveBed(2),
    SwingOffhand(3),
    CriticalEffect(4),
    MagicCriticalEffect(5);

    public companion object {
        public fun fromID(id: Byte): Animations =
            entries.firstOrNull { it.animationId == id } ?: throw IllegalArgumentException("Unknown animation ID")
    }
}