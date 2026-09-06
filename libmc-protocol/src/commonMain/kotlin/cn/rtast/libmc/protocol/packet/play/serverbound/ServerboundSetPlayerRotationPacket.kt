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

public data class ServerboundSetPlayerRotationPacket(
    val yaw: Float,
    val pitch: Float,
    val flags: PlayerPositionFlag,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundSetPlayerRotationPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundSetPlayerRotationPacket) {
            buffer.writeFloat(value.yaw)
            buffer.writeFloat(value.pitch)
            buffer.writeByte(value.flags.flag)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundSetPlayerRotationPacket =
            throw UnsupportedOperationException()
    }
}