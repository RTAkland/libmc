/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.game.player.PlayerPositionFlag

public data class ServerboundSetPlayerPositionPacket(
    val x: Double,
    val feetY: Double,
    val z: Double,
    val flags: PlayerPositionFlag,
) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundSetPlayerPositionPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundSetPlayerPositionPacket) {
            buffer.writeDouble(value.x)
            buffer.writeDouble(value.feetY)
            buffer.writeDouble(value.z)
            buffer.writeByte(value.flags.flag)
        }

        override fun decode(buffer: BytesBuffer): ServerboundSetPlayerPositionPacket =
            throw UnsupportedOperationException()
    }
}