/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writeVarInt
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier
import cn.rtast.libmc.protocol.protocol.util.writeNetworkNBTCompound

public data class ServerboundCustomClickActionPacket(
    val id: Identifier,
    /**
     * payload size, bytes
     */
    val size: Int,
    val payload: NBTCompound,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundCustomClickActionPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundCustomClickActionPacket) {
            buffer.writeIdentifier(value.id)
            buffer.writeVarInt(value.size)
            buffer.writeNetworkNBTCompound(value.payload)
        }

        override fun decode(buffer: BytesBuffer): ServerboundCustomClickActionPacket =
            throw UnsupportedOperationException()
    }
}