/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.configuration.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.packet.configuration.KnownPacks

public data class ServerboundSelectKnownPacksPacket(val knownPacks: List<KnownPacks>) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundSelectKnownPacksPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundSelectKnownPacksPacket) {
            buffer.writeVarInt(value.knownPacks.size)
            value.knownPacks.forEach { KnownPacks.encode(buffer, it) }
        }

        override fun decode(buffer: BytesBuffer): ServerboundSelectKnownPacksPacket =
            throw UnsupportedOperationException()
    }
}