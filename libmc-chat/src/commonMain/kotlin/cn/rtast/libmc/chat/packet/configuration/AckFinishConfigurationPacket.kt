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

internal data object AckFinishConfigurationPacket : MinecraftPacket,
    PacketCodec<AckFinishConfigurationPacket>,
    PacketDirection.ServerboundPacket {
    override val packetId: Int = 0x03

    override fun encode(buffer: _Buffer, value: AckFinishConfigurationPacket) {}

    override fun decode(buffer: _Buffer): AckFinishConfigurationPacket = AckFinishConfigurationPacket
}