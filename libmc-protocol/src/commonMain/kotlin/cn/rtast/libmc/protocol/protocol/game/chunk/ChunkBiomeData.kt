/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.chunk

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readPrefixedByteArray
import cn.rtast.libmc.primitives.writePrefixedByteArray

public data class ChunkBiomeData(val chunkZ: Int, val chunkX: Int, val data: ByteArray) {
    internal companion object Codec : PacketCodec<ChunkBiomeData> {
        override fun encode(buffer: BytesBuffer, value: ChunkBiomeData) {
            buffer.writeInt(value.chunkZ)
            buffer.writeInt(value.chunkX)
            buffer.writePrefixedByteArray(value.data)
        }

        override fun decode(buffer: BytesBuffer): ChunkBiomeData {
            val z = buffer.readInt()
            val x = buffer.readInt()
            val data = buffer.readPrefixedByteArray()
            return ChunkBiomeData(z, x, data)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as ChunkBiomeData
        if (chunkZ != other.chunkZ) return false
        if (chunkX != other.chunkX) return false
        if (!data.contentEquals(other.data)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = chunkZ
        result = 31 * result + chunkX
        result = 31 * result + data.contentHashCode()
        return result
    }
}