/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec

public data object ClientboundResetChatPacket : MinecraftPacket,
    PacketCodec<ClientboundResetChatPacket> {
    override fun encode(buffer: BytesBuffer, value: ClientboundResetChatPacket) {}
    override fun decode(buffer: BytesBuffer): ClientboundResetChatPacket = ClientboundResetChatPacket
}