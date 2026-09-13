/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readVarInt

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Acknowledge_Block_Change
 */
public data class ClientboundAcknowledgeBlockChangePacket(val sequenceId: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundAcknowledgeBlockChangePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundAcknowledgeBlockChangePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundAcknowledgeBlockChangePacket {
            return ClientboundAcknowledgeBlockChangePacket(buffer.readVarInt())
        }
    }
}