/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.status.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data object ServerboundStatusRequestPacket : MinecraftPacket, PacketCodec<ServerboundStatusRequestPacket> {
    override fun encode(buffer: BytesBuffer, value: ServerboundStatusRequestPacket) {}
    override fun decode(buffer: BytesBuffer): ServerboundStatusRequestPacket =
        throw UnsupportedOperationException()
}