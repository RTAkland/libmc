/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec

public data object ServerboundPlayerLoadedPacket : MinecraftPacket, PacketCodec<ServerboundPlayerLoadedPacket> {
    override fun encode(buffer: BytesBuffer, value: ServerboundPlayerLoadedPacket) {}
    override fun decode(buffer: BytesBuffer): ServerboundPlayerLoadedPacket =
        throw UnsupportedOperationException()
}