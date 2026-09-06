/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readMcString
import cn.rtast.libmc.common.readVarInt

public data class ClientboundTransferPacket(val host: String, val port: Int) : ClientboundConfigurationPacket {
    internal companion object Codec : PacketCodec<ClientboundTransferPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundTransferPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundTransferPacket {
            val host = buffer.readMcString()
            val port = buffer.readVarInt()
            return ClientboundTransferPacket(host, port)
        }
    }
}