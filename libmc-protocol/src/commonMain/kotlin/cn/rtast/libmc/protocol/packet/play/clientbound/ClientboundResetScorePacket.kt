/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.*
import cn.rtast.libmc.common.packet.MinecraftPacket

public data class ClientboundResetScorePacket(val entityName: String, val objectiveName: String?) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundResetScorePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundResetScorePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundResetScorePacket {
            val entityName = buffer.readMcString()
            val objectiveName = buffer.readPrefixed { readOptional { readMcString() } }.first() // ?
            return ClientboundResetScorePacket(entityName, objectiveName)
        }
    }
}