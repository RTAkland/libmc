/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Game_Event
 */
public data class ClientboundGameEventPacket(val event: UByte, val value: Float) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundGameEventPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundGameEventPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundGameEventPacket {
            val event = buffer.readUByte()
            val value = buffer.readFloat()
            return ClientboundGameEventPacket(event, value)
        }
    }
}