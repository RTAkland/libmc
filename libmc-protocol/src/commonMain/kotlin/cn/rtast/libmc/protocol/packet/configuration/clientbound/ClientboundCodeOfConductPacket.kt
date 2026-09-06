/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.primitives.readMcString

public data class ClientboundCodeOfConductPacket(val codeOfConduct: String) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundCodeOfConductPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundCodeOfConductPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundCodeOfConductPacket {
            return ClientboundCodeOfConductPacket(buffer.readMcString())
        }
    }
}