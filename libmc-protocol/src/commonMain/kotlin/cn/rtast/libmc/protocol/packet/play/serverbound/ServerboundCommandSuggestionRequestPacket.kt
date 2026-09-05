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

public data class ServerboundCommandSuggestionRequestPacket(
    /**
     * to get transaction id, use `MinecraftClient.transactionManager.nextCommandSuggestionId`
     */
    val transactionId: Int,
    /**
     * All the text behind the cursor including the / (e.g. to the left of the cursor in left-to-right languages like English).
     */
    val text: String,
) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundCommandSuggestionRequestPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundCommandSuggestionRequestPacket) {
            buffer.writeVarInt(value.transactionId)
            buffer.writeMcString(value.text)
        }

        override fun decode(buffer: BytesBuffer): ServerboundCommandSuggestionRequestPacket =
            throw UnsupportedOperationException()
    }
}