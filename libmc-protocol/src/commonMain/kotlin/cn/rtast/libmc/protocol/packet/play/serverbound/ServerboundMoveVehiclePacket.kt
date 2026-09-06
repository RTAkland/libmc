/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Move_Vehicle_(serverbound)
 * onGround does not seem to exist
 */
public data class ServerboundMoveVehiclePacket(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float,
//    val onGround: Boolean
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundMoveVehiclePacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundMoveVehiclePacket) {
            buffer.writeDouble(value.x)
            buffer.writeDouble(value.y)
            buffer.writeDouble(value.z)
            buffer.writeFloat(value.yaw)
            buffer.writeFloat(value.pitch)
        }

        override fun decode(buffer: BytesBuffer): ServerboundMoveVehiclePacket = throw UnsupportedOperationException()
    }
}