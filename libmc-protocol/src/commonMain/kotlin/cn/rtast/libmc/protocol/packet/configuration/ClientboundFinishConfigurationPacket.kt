/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.configuration

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data object ClientboundFinishConfigurationPacket : MinecraftPacket,
    PacketCodec<ClientboundFinishConfigurationPacket> {
    override fun encode(buffer: BytesBuffer, value: ClientboundFinishConfigurationPacket) {}
    override fun decode(buffer: BytesBuffer): ClientboundFinishConfigurationPacket {
        return ClientboundFinishConfigurationPacket
    }
}