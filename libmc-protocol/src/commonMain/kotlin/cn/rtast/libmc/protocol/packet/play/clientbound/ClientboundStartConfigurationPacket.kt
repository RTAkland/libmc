/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec

public data object ClientboundStartConfigurationPacket : MinecraftPacket,
    PacketCodec<ClientboundStartConfigurationPacket> {
    override suspend fun encode(buffer: BytesBuffer, value: ClientboundStartConfigurationPacket) {}
    override suspend fun decode(buffer: BytesBuffer): ClientboundStartConfigurationPacket {
        return ClientboundStartConfigurationPacket
    }
}