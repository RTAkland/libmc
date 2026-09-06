/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.status.serverbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec

public data object ServerboundStatusRequestPacket : MinecraftPacket, PacketCodec<ServerboundStatusRequestPacket> {
    override suspend fun encode(buffer: BytesBuffer, value: ServerboundStatusRequestPacket) {}
    override suspend fun decode(buffer: BytesBuffer): ServerboundStatusRequestPacket =
        throw UnsupportedOperationException()
}