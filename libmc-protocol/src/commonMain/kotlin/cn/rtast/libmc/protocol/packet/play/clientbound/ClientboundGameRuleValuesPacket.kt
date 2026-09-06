/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.protocol.protocol.game.gamerule.GameRuleEntry
import cn.rtast.libmc.protocol.protocol.game.gamerule.readGameRule

public data class ClientboundGameRuleValuesPacket(val rules: List<GameRuleEntry>) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundGameRuleValuesPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundGameRuleValuesPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundGameRuleValuesPacket {
            val count = buffer.readVarInt()
            val rules = ArrayList<GameRuleEntry>(count)
            repeat(count) { rules.add(buffer.readGameRule()) }
            return ClientboundGameRuleValuesPacket(rules)
        }
    }
}