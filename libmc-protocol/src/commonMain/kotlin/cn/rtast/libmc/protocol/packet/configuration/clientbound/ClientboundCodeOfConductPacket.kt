/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readMcString

public data class ClientboundCodeOfConductPacket(val codeOfConduct: String) : ClientboundConfigurationPacket {
    internal companion object Codec : PacketCodec<ClientboundCodeOfConductPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundCodeOfConductPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundCodeOfConductPacket {
            return ClientboundCodeOfConductPacket(buffer.readMcString())
        }
    }
}