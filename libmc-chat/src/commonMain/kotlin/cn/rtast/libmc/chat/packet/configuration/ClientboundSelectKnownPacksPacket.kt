/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.chat.packet.configuration

import cn.rtast.libmc.chat.packet.PacketDirection
import cn.rtast.libmc.common.*

internal data class ClientboundSelectKnownPacksPacket(
    val knownPacks: List<KnownPacks>,
) : MinecraftPacket, PacketDirection.AcrossPacket {
    override val packetId: Int = 0x0e

    companion object Codec : PacketCodec<ClientboundSelectKnownPacksPacket> {
        override fun encode(buffer: _Buffer, value: ClientboundSelectKnownPacksPacket) {
            buffer.writeVarInt(value.knownPacks.size)
            value.knownPacks.forEach { KnownPacks.encode(buffer, it) }
        }

        override fun decode(buffer: _Buffer): ClientboundSelectKnownPacksPacket {
            val packsCount = buffer.readVarInt()
            val packs = List(packsCount) { KnownPacks.decode(buffer) }
            return ClientboundSelectKnownPacksPacket(packs)
        }
    }
}