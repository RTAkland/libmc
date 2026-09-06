/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.*
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.game.block.*

public data class ServerboundSetStructureBlockPacket(
    val location: BlockPos,
    val action: StructureBlockAction,
    val mode: StructureBlockMode,
    val name: String,
    /**
     * Between -48 and 48.
     */
    val offsetX: Byte,
    /**
     * Between -48 and 48.
     */
    val offsetY: Byte,
    /**
     * Between -48 and 48.
     */
    val offsetZ: Byte,
    /**
     * Between 0 and 48.
     */
    val sizeX: Byte,
    /**
     * Between 0 and 48.
     */
    val sizeY: Byte,
    /**
     * Between 0 and 48.
     */
    val sizeZ: Byte,
    val mirror: StructureMirror,
    val rotation: StructureRotation,
    val metadata: String,
    val integrity: Float,
    val seed: Long,
    val ignoreEntities: Boolean,
    val showAir: Boolean,
    val showBoundingBox: Boolean,
    val strictPlacement: Boolean,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundSetStructureBlockPacket> {
        private const val FLAG_IGNORE_ENTITIES = 0x01
        private const val FLAG_SHOW_AIR = 0x02
        private const val FLAG_SHOW_BOUNDING_BOX = 0x04
        private const val FLAG_STRICT_PLACEMENT = 0x08

        override fun encode(buffer: BytesBuffer, value: ServerboundSetStructureBlockPacket) {
            require(value.offsetX in -48..48) { "offsetX must be between -48 and 48" }
            require(value.offsetY in -48..48) { "offsetY must be between -48 and 48" }
            require(value.offsetZ in -48..48) { "offsetZ must be between -48 and 48" }
            require(value.sizeX in 0..48) { "sizeX must be between 0 and 48" }
            require(value.sizeY in 0..48) { "sizeY must be between 0 and 48" }
            require(value.sizeZ in 0..48) { "sizeZ must be between 0 and 48" }
            require(value.integrity in 0.0f..1.0f) { "integrity must be between 0.0 and 1.0" }
            require(value.metadata.length <= 128) { "metadata length must not exceed 128" }
            var flags = 0
            if (value.ignoreEntities) flags = flags or FLAG_IGNORE_ENTITIES
            if (value.showAir) flags = flags or FLAG_SHOW_AIR
            if (value.showBoundingBox) flags = flags or FLAG_SHOW_BOUNDING_BOX
            if (value.strictPlacement) flags = flags or FLAG_STRICT_PLACEMENT
            buffer.writeBlockPos(value.location)
            buffer.writeVarInt(value.action.id)
            buffer.writeVarInt(value.mode.id)
            buffer.writeMcString(value.name)
            buffer.writeByte(value.offsetX)
            buffer.writeByte(value.offsetY)
            buffer.writeByte(value.offsetZ)
            buffer.writeByte(value.sizeX)
            buffer.writeByte(value.sizeY)
            buffer.writeByte(value.sizeZ)
            buffer.writeVarInt(value.mirror.id)
            buffer.writeVarInt(value.rotation.id)
            buffer.writeMcString(value.metadata)
            buffer.writeFloat(value.integrity)
            buffer.writeVarLong(value.seed)
            buffer.writeByte(flags.toByte())
        }

        override fun decode(buffer: BytesBuffer): ServerboundSetStructureBlockPacket =
            throw UnsupportedOperationException()
    }
}