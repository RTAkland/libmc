/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data object ServerboundPlayerLoadedPacket : MinecraftPacket, PacketCodec<ServerboundPlayerLoadedPacket> {
    override fun encode(buffer: BytesBuffer, value: ServerboundPlayerLoadedPacket) {}
    override fun decode(buffer: BytesBuffer): ServerboundPlayerLoadedPacket = throw UnsupportedOperationException()
}