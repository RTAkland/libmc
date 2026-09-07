/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.readBlockPos
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ClientboundBlockEntityDataPacket(val location: BlockPos, val type: Int, val data: NBTCompound) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundBlockEntityDataPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundBlockEntityDataPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundBlockEntityDataPacket {
            val location = buffer.readBlockPos()
            val type = buffer.readVarInt()
            val data = buffer.readNetworkNBTCompound()
            return ClientboundBlockEntityDataPacket(location, type, data)
        }
    }
}