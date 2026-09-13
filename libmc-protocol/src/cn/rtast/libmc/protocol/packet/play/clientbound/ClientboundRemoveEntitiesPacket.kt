/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.primitives.readVarInt

public data class ClientboundRemoveEntitiesPacket(val entityIds: List<Int>): MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundRemoveEntitiesPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundRemoveEntitiesPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundRemoveEntitiesPacket {
            return ClientboundRemoveEntitiesPacket(buffer.readPrefixed { readVarInt() })
        }
    }
}