/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readMcString
import cn.rtast.libmc.primitives.readOptional
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.stream.BytesBuffer

public data class ClientboundResetScorePacket(val entityName: String, val objectiveName: String?) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundResetScorePacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundResetScorePacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundResetScorePacket {
            val entityName = buffer.readMcString()
            val objectiveName = buffer.readPrefixed { readOptional { readMcString() } }.first() // ?
            return ClientboundResetScorePacket(entityName, objectiveName)
        }
    }
}