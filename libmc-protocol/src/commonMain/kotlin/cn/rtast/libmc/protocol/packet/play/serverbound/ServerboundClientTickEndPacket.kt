/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data object ServerboundClientTickEndPacket : MinecraftPacket, PacketCodec<ServerboundClientTickEndPacket> {
    override suspend fun encode(buffer: BytesBuffer, value: ServerboundClientTickEndPacket) {}
    override suspend fun decode(buffer: BytesBuffer): ServerboundClientTickEndPacket = throw UnsupportedOperationException()
}