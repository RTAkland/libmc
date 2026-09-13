/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game.player.action

public enum class PlayerActionStatus(public val id: Int) {
    STARTED_DIGGING(0),
    CANCELLED_DIGGING(1),
    FINISHED_DIGGING(2),
    DROP_ITEM_STACK(3),
    DROP_ITEM(4),
    SHOOT_ARROW_OR_FINISH_EATING(5),
    SWAP_ITEM_IN_HAND(6),
    STAB(7);

    public companion object {
        public fun fromID(id: Int): PlayerActionStatus = entries.first { it.id == id }
    }
}