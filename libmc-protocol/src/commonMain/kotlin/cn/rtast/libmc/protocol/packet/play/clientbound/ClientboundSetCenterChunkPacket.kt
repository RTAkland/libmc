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

public data class ClientboundSetCenterChunkPacket(val chunkX: Int, val chunkZ: Int) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetCenterChunkPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundSetCenterChunkPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundSetCenterChunkPacket {
            val chunkX = buffer.readVarInt()
            val chunkZ = buffer.readVarInt()
            return ClientboundSetCenterChunkPacket(chunkX, chunkZ)
        }
    }
}