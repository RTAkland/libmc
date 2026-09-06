/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.configuration.serverbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data object ServerboundAckFinishConfigurationPacket : MinecraftPacket,
    PacketCodec<ServerboundAckFinishConfigurationPacket> {
    override suspend fun encode(buffer: BytesBuffer, value: ServerboundAckFinishConfigurationPacket) {}
    override suspend fun decode(buffer: BytesBuffer): ServerboundAckFinishConfigurationPacket =
        throw UnsupportedOperationException()
}