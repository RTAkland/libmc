/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.configuration.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.registry.report.ServerLink

public data class ClientboundServerLinksPacket(val links: List<ServerLink>) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundServerLinksPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundServerLinksPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundServerLinksPacket {
            val count = buffer.readVarInt()
            val links = ArrayList<ServerLink>(count)
            repeat(count) { links.add(ServerLink.decode(buffer)) }
            return ClientboundServerLinksPacket(links)
        }
    }
}