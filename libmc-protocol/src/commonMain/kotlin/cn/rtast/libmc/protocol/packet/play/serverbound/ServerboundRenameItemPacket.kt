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

public data class ServerboundRenameItemPacket(val itemName: String) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundRenameItemPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundRenameItemPacket) {
            require(value.itemName.length < 50)
            buffer.writeMcString(value.itemName)
        }

        override fun decode(buffer: BytesBuffer): ServerboundRenameItemPacket =
            throw UnsupportedOperationException()
    }
}