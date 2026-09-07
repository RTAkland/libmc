/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.game.chat.readTextComponent
import cn.rtast.libmc.stream.BytesBuffer

public data class ClientboundPlayerCombatDeathPacket(val playerId: Int, val message: TextComponent) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundPlayerCombatDeathPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundPlayerCombatDeathPacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundPlayerCombatDeathPacket {
            val playerId = buffer.readVarInt()
            val message = buffer.readTextComponent()
            return ClientboundPlayerCombatDeathPacket(playerId, message)
        }
    }
}