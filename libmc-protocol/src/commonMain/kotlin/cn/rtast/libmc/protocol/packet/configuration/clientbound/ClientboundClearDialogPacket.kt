/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec

public data object ClientboundClearDialogPacket : MinecraftPacket,
    PacketCodec<ClientboundClearDialogPacket> {
    override suspend fun encode(buffer: BytesBuffer, value: ClientboundClearDialogPacket) {}
    override suspend fun decode(buffer: BytesBuffer): ClientboundClearDialogPacket = ClientboundClearDialogPacket
}