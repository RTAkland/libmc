/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.player.Hand

public data class ClientboundOpenBookPacket(val hand: Hand) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundOpenBookPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundOpenBookPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundOpenBookPacket {
            return ClientboundOpenBookPacket(Hand.fromID(buffer.readVarInt()))
        }
    }
}