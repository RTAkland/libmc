/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.readPrefixed
import cn.rtast.libmc.common.readVarInt

public data class ClientboundRemoveEntitiesPacket(val entityIds: List<Int>): MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundRemoveEntitiesPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundRemoveEntitiesPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundRemoveEntitiesPacket {
            return ClientboundRemoveEntitiesPacket(buffer.readPrefixed { readVarInt() })
        }
    }
}