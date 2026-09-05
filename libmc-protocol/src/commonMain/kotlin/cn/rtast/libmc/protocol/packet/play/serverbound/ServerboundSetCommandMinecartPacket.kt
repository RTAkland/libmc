/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writeMcString
import cn.rtast.libmc.common.writeVarInt

public data class ServerboundSetCommandMinecartPacket(
    val entityId: Int,
    val command: String,
    /**
     * If false, the output of the previous command will not be stored within the command block.
     */
    val trackOutput: Boolean,
) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundSetCommandMinecartPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundSetCommandMinecartPacket) {
            buffer.writeVarInt(value.entityId)
            buffer.writeMcString(value.command)
            buffer.writeBoolean(value.trackOutput)
        }

        override fun decode(buffer: BytesBuffer): ServerboundSetCommandMinecartPacket =
            throw UnsupportedOperationException()
    }
}