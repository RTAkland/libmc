/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.login.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec

public data object ServerboundLoginAcknowledgedPacket : MinecraftPacket,
    PacketCodec<ServerboundLoginAcknowledgedPacket> {
    override fun encode(buffer: BytesBuffer, value: ServerboundLoginAcknowledgedPacket) {}
    override fun decode(buffer: BytesBuffer): ServerboundLoginAcknowledgedPacket =
        throw UnsupportedOperationException()
}