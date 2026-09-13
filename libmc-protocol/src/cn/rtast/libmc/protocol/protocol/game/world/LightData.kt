/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.protocol.game.world

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.*

public data class LightData(
    val skyLightMask: BitSet,
    val blockLightMask: BitSet,
    val emptySkyLightMask: BitSet,
    val emptyBlockLightMask: BitSet,
    val skyLightArrays: List<ByteArray>,  // fixed 2048
    val blockLightArrays: List<ByteArray>,  // fixed 2048
) {
    internal companion object Codec : PacketCodec<LightData> {
        private const val LIGHT_ARRAY_SIZE = 2048
        override fun encode(buffer: BytesBuffer, value: LightData) {
            buffer.writeBitSet(value.skyLightMask)
            buffer.writeBitSet(value.blockLightMask)
            buffer.writeBitSet(value.emptySkyLightMask)
            buffer.writeBitSet(value.emptyBlockLightMask)
            buffer.writeVarInt(value.skyLightArrays.size)
            buffer.writePrefixed(value.skyLightArrays) { array ->
                require(array.size == LIGHT_ARRAY_SIZE)
                writeVarInt(array.size)
                writeBytes(array)
            }

            buffer.writePrefixed(value.blockLightArrays) { array ->
                require(array.size == LIGHT_ARRAY_SIZE)
                writeVarInt(array.size)
                writeBytes(array)
            }
        }

        override fun decode(buffer: BytesBuffer): LightData {
            val skyLightMask = buffer.readBitSet()
            val blockLightMask = buffer.readBitSet()
            val emptySkyLightMask = buffer.readBitSet()
            val emptyBlockLightMask = buffer.readBitSet()
            val skyLightArrays = buffer.readPrefixed {
                val length = readVarInt()
                require(length == LIGHT_ARRAY_SIZE)
                readBytes(length)
            }
            val blockLightArrays = buffer.readPrefixed {
                val length = readVarInt()
                require(length == LIGHT_ARRAY_SIZE)
                readBytes(length)
            }
            return LightData(
                skyLightMask, blockLightMask, emptySkyLightMask,
                emptyBlockLightMask, skyLightArrays, blockLightArrays
            )
        }
    }

    public fun hasSkyLight(sectionY: Int, minSectionY: Int): Boolean {
        val bitIndex = sectionY - (minSectionY - 1)
        return skyLightMask.getBit(bitIndex)
    }


    public fun hasBlockLight(sectionY: Int, minSectionY: Int): Boolean {
        val bitIndex = sectionY - (minSectionY - 1)
        return blockLightMask.getBit(bitIndex)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as LightData
        if (!skyLightMask.contentEquals(other.skyLightMask)) return false
        if (!blockLightMask.contentEquals(other.blockLightMask)) return false
        if (!emptySkyLightMask.contentEquals(other.emptySkyLightMask)) return false
        if (!emptyBlockLightMask.contentEquals(other.emptyBlockLightMask)) return false
        if (skyLightArrays != other.skyLightArrays) return false
        if (blockLightArrays != other.blockLightArrays) return false
        return true
    }

    override fun hashCode(): Int {
        var result = skyLightMask.contentHashCode()
        result = 31 * result + blockLightMask.contentHashCode()
        result = 31 * result + emptySkyLightMask.contentHashCode()
        result = 31 * result + emptyBlockLightMask.contentHashCode()
        result = 31 * result + skyLightArrays.hashCode()
        result = 31 * result + blockLightArrays.hashCode()
        return result
    }
}