/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.world.LightData

public data class ClientboundLightUpdatePacket(
    /**
     * Chunk coordinate (block coordinate divided by 16, rounded down)
     */
    val chunkX: Int,
    /**
     * Chunk coordinate (block coordinate divided by 16, rounded down)
     */
    val chunkZ: Int,
    val data: LightData,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundLightUpdatePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundLightUpdatePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundLightUpdatePacket {
            val chunkX = buffer.readVarInt()
            val chunkZ = buffer.readVarInt()
            val data = LightData.decode(buffer)
            return ClientboundLightUpdatePacket(chunkX, chunkZ, data)
        }
    }
}