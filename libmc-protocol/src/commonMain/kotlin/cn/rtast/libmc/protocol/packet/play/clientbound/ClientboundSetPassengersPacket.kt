/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.primitives.readVarInt

public data class ClientboundSetPassengersPacket(val entityId: Int, val passengersId: List<Int>) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetPassengersPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundSetPassengersPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundSetPassengersPacket {
            val entityId = buffer.readVarInt()
            val passengers = buffer.readPrefixed { readVarInt() }
            return ClientboundSetPassengersPacket(entityId, passengers)
        }
    }
}