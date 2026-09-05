/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game.player

public enum class PlayerCommandAction(public val id: Int) {
    LEAVE_BED(0),
    START_SPRINTING(1),
    STOP_SPRINTING(2),
    START_JUMP_WITH_HORSE(3),
    STOP_JUMP_WITH_HORSE(4),
    OPEN_VEHICLE_INVENTORY(5),
    START_FLYING_WITH_ELYTRA(6);

    public companion object {
        public fun fromID(id: Int): PlayerCommandAction = entries.first { it.id == id }
    }
}