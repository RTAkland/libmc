/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.packet.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.primitives.readOptional
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier

public data class ClientboundSelectAdvancementsTabPacket(val tabId: Identifier?) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSelectAdvancementsTabPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundSelectAdvancementsTabPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundSelectAdvancementsTabPacket {
            return ClientboundSelectAdvancementsTabPacket(buffer.readOptional { readIdentifier() })
        }
    }
}