/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.player.PlayerCommandAction

public data class ServerboundPlayerCommandPacket(
    val entityId: Int,
    val action: PlayerCommandAction,
    /**
     * Only used by the “start jump with horse” action,
     * in which case it ranges from 0 to 100. In all other cases it is 0
     */
    val jumpBoost: Int,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundPlayerCommandPacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ServerboundPlayerCommandPacket) {
            buffer.writeVarInt(value.entityId)
            buffer.writeVarInt(value.action.id)
            buffer.writeVarInt(value.jumpBoost)
        }

        override suspend fun decode(buffer: BytesBuffer): ServerboundPlayerCommandPacket =
            throw UnsupportedOperationException()
    }
}