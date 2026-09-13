/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.status.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec

public data object ServerboundStatusRequestPacket : MinecraftPacket, PacketCodec<ServerboundStatusRequestPacket> {
    override fun encode(buffer: BytesBuffer, value: ServerboundStatusRequestPacket) {}
    override fun decode(buffer: BytesBuffer): ServerboundStatusRequestPacket =
        throw UnsupportedOperationException()
}