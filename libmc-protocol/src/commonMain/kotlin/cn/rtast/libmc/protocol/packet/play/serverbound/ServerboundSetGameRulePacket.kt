/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writeMcString
import cn.rtast.libmc.common.writePrefixedArray
import cn.rtast.libmc.protocol.protocol.game.gamerule.GameRuleEntry

public data class ServerboundSetGameRulePacket(val rules: List<GameRuleEntry>) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundSetGameRulePacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundSetGameRulePacket) {
            buffer.writePrefixedArray(value.rules) { rule ->
                writeMcString(rule.name)
                writeMcString(rule.value)
            }
        }

        override fun decode(buffer: BytesBuffer): ServerboundSetGameRulePacket =
            throw UnsupportedOperationException()
    }
}