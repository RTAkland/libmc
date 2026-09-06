/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.packet.PacketCodec

public data object ServerboundPlayerLoadedPacket : MinecraftPacket, PacketCodec<ServerboundPlayerLoadedPacket> {
    override suspend fun encode(buffer: BytesBuffer, value: ServerboundPlayerLoadedPacket) {}
    override suspend fun decode(buffer: BytesBuffer): ServerboundPlayerLoadedPacket =
        throw UnsupportedOperationException()
}