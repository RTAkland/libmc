/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec

public data object ClientboundFinishConfigurationPacket : MinecraftPacket,
    PacketCodec<ClientboundFinishConfigurationPacket> {
    override suspend fun encode(buffer: BytesBuffer, value: ClientboundFinishConfigurationPacket) {}
    override suspend fun decode(buffer: BytesBuffer): ClientboundFinishConfigurationPacket {
        return ClientboundFinishConfigurationPacket
    }
}