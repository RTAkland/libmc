/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.chat.packet.configuration

import cn.rtast.libmc.chat.packet.PacketDirection
import cn.rtast.libmc.common.*

internal data class ServerboundSelectKnownPacksPacket(
    val knownPacks: List<KnownPacks>,
) : MinecraftPacket, PacketDirection.AcrossPacket {
    override val packetId: Int = 0x07

    companion object Codec : PacketCodec<ServerboundSelectKnownPacksPacket> {
        override fun encode(buffer: _Buffer, value: ServerboundSelectKnownPacksPacket) {
            buffer.writeVarInt(value.knownPacks.size)
            value.knownPacks.forEach { KnownPacks.encode(buffer, it) }
        }

        override fun decode(buffer: _Buffer): ServerboundSelectKnownPacksPacket {
            val packsCount = buffer.readVarInt()
            val packs = List(packsCount) { KnownPacks.decode(buffer) }
            return ServerboundSelectKnownPacksPacket(packs)
        }
    }
}