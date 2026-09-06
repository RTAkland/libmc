/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.player.Hand

public data class ClientboundOpenBookPacket(val hand: Hand) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundOpenBookPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundOpenBookPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundOpenBookPacket {
            return ClientboundOpenBookPacket(Hand.fromID(buffer.readVarInt()))
        }
    }
}