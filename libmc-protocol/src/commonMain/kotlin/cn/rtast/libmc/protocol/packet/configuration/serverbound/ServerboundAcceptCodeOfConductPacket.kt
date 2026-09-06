/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.serverbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data object ServerboundAcceptCodeOfConductPacket : MinecraftPacket,
    PacketCodec<ServerboundAcceptCodeOfConductPacket> {
    override suspend fun encode(buffer: BytesBuffer, value: ServerboundAcceptCodeOfConductPacket) {}
    override suspend fun decode(buffer: BytesBuffer): ServerboundAcceptCodeOfConductPacket =
        ServerboundAcceptCodeOfConductPacket
}