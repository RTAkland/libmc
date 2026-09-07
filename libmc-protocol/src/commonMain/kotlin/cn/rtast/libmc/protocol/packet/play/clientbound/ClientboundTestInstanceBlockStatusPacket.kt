/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readOptional
import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.game.chat.readTextComponent
import cn.rtast.libmc.stream.BytesBuffer

public data class ClientboundTestInstanceBlockStatusPacket(
    val status: TextComponent,
    val hasSize: Boolean,
    val sizeX: Double?,
    val sizeY: Double?,
    val sizeZ: Double?,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundTestInstanceBlockStatusPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundTestInstanceBlockStatusPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundTestInstanceBlockStatusPacket {
            val status = buffer.readTextComponent()
            val hasSize = buffer.readBoolean()
            val sizeX = buffer.readOptional { readDouble() }  // ?
            val sizeY = buffer.readOptional { readDouble() }  // ?
            val sizeZ = buffer.readOptional { readDouble() }  // ?
            return ClientboundTestInstanceBlockStatusPacket(status, hasSize, sizeX, sizeY, sizeZ)
        }
    }
}