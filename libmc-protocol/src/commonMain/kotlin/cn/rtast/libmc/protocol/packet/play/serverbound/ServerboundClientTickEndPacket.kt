/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket

public data object ServerboundClientTickEndPacket : MinecraftPacket, PacketCodec<ServerboundClientTickEndPacket> {
    override fun encode(buffer: BytesBuffer, value: ServerboundClientTickEndPacket) {}
    override fun decode(buffer: BytesBuffer): ServerboundClientTickEndPacket = throw UnsupportedOperationException()
}