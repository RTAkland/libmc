/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.readVarInt

public data class ClientboundSetCarriedItemPacket(val slot: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetCarriedItemPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetCarriedItemPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetCarriedItemPacket {
            return ClientboundSetCarriedItemPacket(buffer.readVarInt())
        }
    }
}