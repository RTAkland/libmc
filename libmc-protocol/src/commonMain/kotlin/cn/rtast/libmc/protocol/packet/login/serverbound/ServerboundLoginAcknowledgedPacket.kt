/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.login.serverbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec

public data object ServerboundLoginAcknowledgedPacket : MinecraftPacket,
    PacketCodec<ServerboundLoginAcknowledgedPacket> {
    override suspend fun encode(buffer: BytesBuffer, value: ServerboundLoginAcknowledgedPacket) {}
    override suspend fun decode(buffer: BytesBuffer): ServerboundLoginAcknowledgedPacket =
        throw UnsupportedOperationException()
}