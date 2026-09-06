/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.status.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readMcString

public data class ClientboundStatusResponsePacket(val jsonResponse: String) : ClientboundStatusPacket {
    internal companion object Codec : PacketCodec<ClientboundStatusResponsePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundStatusResponsePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundStatusResponsePacket =
            ClientboundStatusResponsePacket(buffer.readMcString())
    }
}