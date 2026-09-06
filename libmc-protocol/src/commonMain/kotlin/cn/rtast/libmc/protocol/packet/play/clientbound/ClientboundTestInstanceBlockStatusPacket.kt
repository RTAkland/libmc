/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.primitives.readOptional
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ClientboundTestInstanceBlockStatusPacket(
    val status: NBTCompound,
    val hasSize: Boolean,
    val sizeX: Double?,
    val sizeY: Double?,
    val sizeZ: Double?,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundTestInstanceBlockStatusPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundTestInstanceBlockStatusPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundTestInstanceBlockStatusPacket {
            val status = buffer.readNetworkNBTCompound()
            val hasSize = buffer.readBoolean()
            val sizeX = buffer.readOptional { readDouble() }  // ?
            val sizeY = buffer.readOptional { readDouble() }  // ?
            val sizeZ = buffer.readOptional { readDouble() }  // ?
            return ClientboundTestInstanceBlockStatusPacket(status, hasSize, sizeX, sizeY, sizeZ)
        }
    }
}