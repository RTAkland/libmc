/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.advancement.AdvancementAction
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier

public data class ServerboundSeenAdvancementsPacket(val action: AdvancementAction, val tabId: Identifier?) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundSeenAdvancementsPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundSeenAdvancementsPacket) {
            buffer.writeVarInt(value.action.id)
            if (value.action == AdvancementAction.OPENED_TAB) {
                requireNotNull(value.tabId) { "tabId must not be null when action is OPENED_TAB" }
                buffer.writeIdentifier(value.tabId)
            } else require(value.tabId == null) { "tabId must be null when action is CLOSED_SCREEN" }
        }

        override fun decode(buffer: BytesBuffer): ServerboundSeenAdvancementsPacket =
            throw UnsupportedOperationException()
    }
}