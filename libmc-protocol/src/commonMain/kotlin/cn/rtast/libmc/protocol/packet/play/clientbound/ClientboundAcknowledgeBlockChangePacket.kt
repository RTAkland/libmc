/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readVarInt

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Acknowledge_Block_Change
 */
public data class ClientboundAcknowledgeBlockChangePacket(val sequenceId: Int) : ClientboundPlayPacket {
    public companion object Codec : PacketCodec<ClientboundAcknowledgeBlockChangePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundAcknowledgeBlockChangePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundAcknowledgeBlockChangePacket {
            return ClientboundAcknowledgeBlockChangePacket(buffer.readVarInt())
        }
    }
}