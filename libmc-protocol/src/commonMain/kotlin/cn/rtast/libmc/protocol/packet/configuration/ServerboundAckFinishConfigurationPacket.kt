/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.configuration

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data object ServerboundAckFinishConfigurationPacket : MinecraftPacket,
    PacketCodec<ServerboundAckFinishConfigurationPacket> {
    override fun encode(buffer: BytesBuffer, value: ServerboundAckFinishConfigurationPacket) {}

    override fun decode(buffer: BytesBuffer): ServerboundAckFinishConfigurationPacket =
        ServerboundAckFinishConfigurationPacket
}