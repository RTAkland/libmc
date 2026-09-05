/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writePrefixedVarIntArray
import cn.rtast.libmc.protocol.protocol.game.debug.DebugSubscriptionType

public data class ServerboundDebugSubscriptionRequestPacket(val subscriptions: List<DebugSubscriptionType>) :
    MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundDebugSubscriptionRequestPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundDebugSubscriptionRequestPacket) {
            buffer.writePrefixedVarIntArray(value.subscriptions.map { it.id })
        }

        override fun decode(buffer: BytesBuffer): ServerboundDebugSubscriptionRequestPacket =
            throw UnsupportedOperationException()
    }
}