/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.*
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.game.world.WorldClockData

public data class ClientboundSetTimePacket(val worldAge: Long, val clocks: List<WorldClockData>) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetTimePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetTimePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetTimePacket {
            val age = buffer.readLong()
            val clocks = buffer.readPrefixed {
                WorldClockData(readVarInt(), readVarLong(), readFloat(), readFloat())
            }
            return ClientboundSetTimePacket(age, clocks)
        }
    }
}