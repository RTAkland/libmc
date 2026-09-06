/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data object ServerboundAcceptCodeOfConductPacket : MinecraftPacket,
    PacketCodec<ServerboundAcceptCodeOfConductPacket> {
    override fun encode(buffer: BytesBuffer, value: ServerboundAcceptCodeOfConductPacket) {}
    override fun decode(buffer: BytesBuffer): ServerboundAcceptCodeOfConductPacket =
        ServerboundAcceptCodeOfConductPacket
}