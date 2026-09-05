/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public object ServerboundConfigurationAcknowledgedPacket : MinecraftPacket,
    PacketCodec<ServerboundConfigurationAcknowledgedPacket> {
    override fun encode(buffer: BytesBuffer, value: ServerboundConfigurationAcknowledgedPacket) {}
    override fun decode(buffer: BytesBuffer): ServerboundConfigurationAcknowledgedPacket {
        return ServerboundConfigurationAcknowledgedPacket
    }
}