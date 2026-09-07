/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec

public data object ClientboundStartConfigurationPacket : MinecraftPacket,
    PacketCodec<ClientboundStartConfigurationPacket> {
    override fun encode(buffer: BytesBuffer, value: ClientboundStartConfigurationPacket) {}
    override fun decode(buffer: BytesBuffer): ClientboundStartConfigurationPacket {
        return ClientboundStartConfigurationPacket
    }
}