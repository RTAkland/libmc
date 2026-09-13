/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.protocol.protocol.game.world.waypoint

public enum class WaypointOperation(public val id: Int) {
    TRACK(0),
    UNTRACK(1),
    UPDATE(2);

    public companion object {
        public fun fromID(id: Int): WaypointOperation = entries.find { it.id == id } ?: TRACK
    }
}