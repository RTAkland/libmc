/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.packet.configuration.KnownPacks

public data class ClientboundSelectKnownPacksPacket(val knownPacks: List<KnownPacks>) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSelectKnownPacksPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSelectKnownPacksPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSelectKnownPacksPacket {
            val packsCount = buffer.readVarInt()
            val packs = List(packsCount) { KnownPacks.decode(buffer) }
            return ClientboundSelectKnownPacksPacket(packs)
        }
    }
}