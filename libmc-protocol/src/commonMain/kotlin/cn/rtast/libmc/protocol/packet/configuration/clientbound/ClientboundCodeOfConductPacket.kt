/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readMcString

public data class ClientboundCodeOfConductPacket(val codeOfConduct: String) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundCodeOfConductPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundCodeOfConductPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundCodeOfConductPacket {
            return ClientboundCodeOfConductPacket(buffer.readMcString())
        }
    }
}