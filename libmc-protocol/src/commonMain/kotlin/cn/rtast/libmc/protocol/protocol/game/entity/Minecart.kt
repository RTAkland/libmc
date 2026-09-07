/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.entity

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.protocol.protocol.game.math.Angle
import cn.rtast.libmc.protocol.protocol.game.math.Vec3d
import cn.rtast.libmc.protocol.protocol.game.math.readAngle
import cn.rtast.libmc.protocol.protocol.game.math.readVec3d

public data class MinecartStep(
    val position: Vec3d,
    val velocity: Vec3d,
    val yaw: Angle,
    val pitch: Angle,
    val weight: Float,
)

internal suspend fun BytesBuffer.readMinecartStep(): MinecartStep {
    val position = readVec3d()!!
    val velocity = readVec3d()!!
    val yaw = readAngle()
    val pitch = readAngle()
    val weight = readFloat()
    return MinecartStep(position, velocity, yaw, pitch, weight)
}