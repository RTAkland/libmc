/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.configuration

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.common.writeVarInt

public data class ClientboundSelectKnownPacksPacket(val knownPacks: List<KnownPacks>) : MinecraftPacket {
    public companion object Codec : PacketCodec<ClientboundSelectKnownPacksPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSelectKnownPacksPacket) {
            buffer.writeVarInt(value.knownPacks.size)
            value.knownPacks.forEach { KnownPacks.encode(buffer, it) }
        }

        override fun decode(buffer: BytesBuffer): ClientboundSelectKnownPacksPacket {
            val packsCount = buffer.readVarInt()
            val packs = List(packsCount) { KnownPacks.decode(buffer) }
            return ClientboundSelectKnownPacksPacket(packs)
        }
    }
}