/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.readVarInt

public data class ClientboundSetCarriedItemPacket(val slot: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetCarriedItemPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundSetCarriedItemPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundSetCarriedItemPacket {
            return ClientboundSetCarriedItemPacket(buffer.readVarInt())
        }
    }
}