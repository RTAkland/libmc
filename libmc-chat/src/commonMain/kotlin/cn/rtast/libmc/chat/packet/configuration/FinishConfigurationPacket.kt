/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.chat.packet.configuration

import cn.rtast.libmc.chat.packet.PacketDirection
import cn.rtast.libmc.common.MinecraftPacket
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common._Buffer

internal data object FinishConfigurationPacket : MinecraftPacket,
    PacketCodec<FinishConfigurationPacket>,
    PacketDirection.ClientboundPacket {
    override val packetId: Int = 0x03

    override fun encode(buffer: _Buffer, value: FinishConfigurationPacket) {}

    override fun decode(buffer: _Buffer): FinishConfigurationPacket = FinishConfigurationPacket
}