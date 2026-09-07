/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.gamerule.GameRuleEntry
import cn.rtast.libmc.protocol.protocol.game.gamerule.readGameRule

public data class ClientboundGameRuleValuesPacket(val rules: List<GameRuleEntry>) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundGameRuleValuesPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundGameRuleValuesPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundGameRuleValuesPacket {
            val count = buffer.readVarInt()
            val rules = ArrayList<GameRuleEntry>(count)
            repeat(count) { rules.add(buffer.readGameRule()) }
            return ClientboundGameRuleValuesPacket(rules)
        }
    }
}