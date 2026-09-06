/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.configuration.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writeVarInt
import cn.rtast.libmc.protocol.packet.configuration.KnownPacks

public data class ServerboundSelectKnownPacksPacket(val knownPacks: List<KnownPacks>) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundSelectKnownPacksPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundSelectKnownPacksPacket) {
            buffer.writeVarInt(value.knownPacks.size)
            value.knownPacks.forEach { KnownPacks.encode(buffer, it) }
        }

        override fun decode(buffer: BytesBuffer): ServerboundSelectKnownPacksPacket =
            throw UnsupportedOperationException()
    }
}