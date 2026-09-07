/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.protocol.protocol.game.world.waypoint

public sealed class WaypointData {
    public enum class Type(public val id: Int) {
        EMPTY(0),
        VEC3I(1),
        CHUNK(2),
        AZIMUTH(3);

        public companion object {
            public fun fromID(id: Int): Type = entries.find { it.id == id } ?: EMPTY
        }
    }

    public abstract val type: Type

    public data object Empty : WaypointData() {
        override val type: Type = Type.EMPTY
    }

    public data class Vec3i(val x: Int, val y: Int, val z: Int) : WaypointData() {
        override val type: Type = Type.VEC3I
    }

    public data class Chunk(val x: Int, val z: Int) : WaypointData() {
        override val type: Type = Type.CHUNK
    }

    public data class Azimuth(val angle: Float) : WaypointData() {
        override val type: Type = Type.AZIMUTH
    }
}