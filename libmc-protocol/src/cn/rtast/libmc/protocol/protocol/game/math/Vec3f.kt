/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/9
 */


package cn.rtast.libmc.protocol.protocol.game.math

import cn.rtast.libmc.network.BytesBuffer

public data class Vec3f(val x: Float, val y: Float, val z: Float) {
    public companion object {
        public val ZERO: Vec3f = Vec3f(0f, 0f, 0f)
    }
}

internal fun BytesBuffer.readVec3f(): Vec3f {
    val x = readFloat()
    val y = readFloat()
    val z = readFloat()
    return Vec3f(x, y, z)
}

internal fun BytesBuffer.writeVec3f(value: Vec3f) {
    writeFloat(value.x)
    writeFloat(value.y)
    writeFloat(value.z)
}