/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec

public data class ServerboundSetPlayerPositionPacket(
    val x: Double,
    val feetY: Double,
    val z: Double,
    val onGround: Boolean,
    val pushingAgainstWall: Boolean = false,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundSetPlayerPositionPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundSetPlayerPositionPacket) {
            buffer.writeDouble(value.x)
            buffer.writeDouble(value.feetY)
            buffer.writeDouble(value.z)
            var flags = 0
            if (value.onGround) flags = flags or 0x01
            if (value.pushingAgainstWall) flags = flags or 0x02
            buffer.writeByte(flags.toByte())
        }

        override fun decode(buffer: BytesBuffer): ServerboundSetPlayerPositionPacket =
            throw UnsupportedOperationException()
    }
}