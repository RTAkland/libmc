/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec

public data object ClientboundDelimiterPacket : MinecraftPacket, PacketCodec<ClientboundDelimiterPacket> {
    override fun encode(buffer: BytesBuffer, value: ClientboundDelimiterPacket) {}
    override fun decode(buffer: BytesBuffer): ClientboundDelimiterPacket {
        return ClientboundDelimiterPacket
    }
}