/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data class ClientboundUnloadChunkPacket(
    /**
     * Block coordinate divided by 16, rounded down
     */
    val chunkZ: Int,
    /**
     * Block coordinate divided by 16, rounded down
     */
    val chunkX: Int,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundUnloadChunkPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundUnloadChunkPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundUnloadChunkPacket {
            val z = buffer.readInt()
            val x = buffer.readInt()
            return ClientboundUnloadChunkPacket(z, x)
        }
    }
}