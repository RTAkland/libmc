/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.math

import cn.rtast.libmc.network.BytesBuffer

public data class Vec3d(val x: Double, val y: Double, val z: Double) {
    public companion object {
        public val ZERO: Vec3d = Vec3d(0.0, 0.0, 0.0)
    }
}

internal fun BytesBuffer.readVec3d(optional: Boolean = false): Vec3d? {
    if (optional && !this.readBoolean()) return null
    val x = this.readDouble()
    val y = this.readDouble()
    val z = this.readDouble()
    return Vec3d(x, y, z)
}

internal fun BytesBuffer.writeVec3d(vec3d: Vec3d?, optional: Boolean = false) {
    if (optional) {
        this.writeBoolean(vec3d != null)
        if (vec3d == null) return
    }
    val value = requireNotNull(vec3d) { "Vec3d cannot be null when optional is false" }
    this.writeDouble(value.x)
    this.writeDouble(value.y)
    this.writeDouble(value.z)
}