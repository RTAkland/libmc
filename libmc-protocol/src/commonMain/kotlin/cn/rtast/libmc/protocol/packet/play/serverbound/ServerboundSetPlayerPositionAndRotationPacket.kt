/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.game.player.PlayerPositionFlag

public data class ServerboundSetPlayerPositionAndRotationPacket(
    val x: Double,
    /**
     * Absolute feet position, normally Head Y - 1.62.
     */
    val feetY: Double,
    val z: Double,
    /**
     * Absolute rotation on the X Axis, in degrees.
     */
    val yaw: Float,
    /**
     * Absolute rotation on the Y Axis, in degrees.
     */
    val pitch: Float,
    val flags: PlayerPositionFlag,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundSetPlayerPositionAndRotationPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundSetPlayerPositionAndRotationPacket) {
            buffer.writeDouble(value.x)
            buffer.writeDouble(value.feetY)
            buffer.writeDouble(value.z)
            buffer.writeFloat(value.yaw)
            buffer.writeFloat(value.pitch)
            buffer.writeByte(value.flags.flag)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundSetPlayerPositionAndRotationPacket =
            throw UnsupportedOperationException()
    }
}