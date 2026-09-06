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
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.CommandBlockMode
import cn.rtast.libmc.protocol.protocol.game.block.writeBlockPos

public data class ServerboundSetCommandBlockPacket(
    val location: BlockPos,
    val command: String,
    val mode: CommandBlockMode,
    val trackOutput: Boolean,
    val conditional: Boolean,
    val automatic: Boolean,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundSetCommandBlockPacket> {
        private const val FLAG_TRACK_OUTPUT = 0x01
        private const val FLAG_CONDITIONAL = 0x02
        private const val FLAG_AUTOMATIC = 0x04

        override fun encode(buffer: BytesBuffer, value: ServerboundSetCommandBlockPacket) {
            var flags = 0
            if (value.trackOutput) flags = flags or FLAG_TRACK_OUTPUT
            if (value.conditional) flags = flags or FLAG_CONDITIONAL
            if (value.automatic) flags = flags or FLAG_AUTOMATIC
            buffer.writeBlockPos(value.location)
            buffer.writeMcString(value.command)
            buffer.writeVarInt(value.mode.id)
            buffer.writeByte(flags.toByte())
        }

        override fun decode(buffer: BytesBuffer): ServerboundSetCommandBlockPacket =
            throw UnsupportedOperationException()
    }
}