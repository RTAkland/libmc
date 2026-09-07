/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.readVarLong
import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.protocol.protocol.game.world.WorldClockData

public data class ClientboundSetTimePacket(val worldAge: Long, val clocks: List<WorldClockData>) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetTimePacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundSetTimePacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundSetTimePacket {
            val age = buffer.readLong()
            val clocks = buffer.readPrefixed {
                WorldClockData(readVarInt(), readVarLong(), readFloat(), readFloat())
            }
            return ClientboundSetTimePacket(age, clocks)
        }
    }
}