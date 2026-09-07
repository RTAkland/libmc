/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.block.*
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier
import cn.rtast.libmc.protocol.protocol.util.writeNetworkNBTCompound

public data class ServerboundTestInstanceBlockActionPacket(
    val position: BlockPos,
    val action: TestInstanceBlockAction,
    val test: Identifier?,
    val sizeX: Int,
    val sizeY: Int,
    val sizeZ: Int,
    val rotation: TestInstanceRotation,
    val ignoreEntities: Boolean,
    val status: TestInstanceStatus,
    val errorMessage: NBTCompound?,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundTestInstanceBlockActionPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundTestInstanceBlockActionPacket) {
            buffer.writeBlockPos(value.position)
            buffer.writeVarInt(value.action.id)
            buffer.writeBoolean(value.test != null)
            if (value.test != null) buffer.writeIdentifier(value.test)
            buffer.writeVarInt(value.sizeX)
            buffer.writeVarInt(value.sizeY)
            buffer.writeVarInt(value.sizeZ)
            buffer.writeVarInt(value.rotation.id)
            buffer.writeBoolean(value.ignoreEntities)
            buffer.writeVarInt(value.status.id)
            buffer.writeBoolean(value.errorMessage != null)
            if (value.errorMessage != null) buffer.writeNetworkNBTCompound(value.errorMessage)  // TODO to fix
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundTestInstanceBlockActionPacket =
            throw UnsupportedOperationException()
    }
}