/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.writeMcString

public data class ServerboundRenameItemPacket(val itemName: String) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundRenameItemPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundRenameItemPacket) {
            require(value.itemName.length < 50)
            buffer.writeMcString(value.itemName)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundRenameItemPacket =
            throw UnsupportedOperationException()
    }
}