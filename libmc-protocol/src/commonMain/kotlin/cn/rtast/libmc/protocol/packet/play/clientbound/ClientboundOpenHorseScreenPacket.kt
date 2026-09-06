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

public data class ClientboundOpenHorseScreenPacket(
    val windowId: Int,
    val columnsCount: Int,
    val entityId: Int,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundOpenHorseScreenPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundOpenHorseScreenPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundOpenHorseScreenPacket {
            val windowId = buffer.readVarInt()
            val columnsCount = buffer.readVarInt()
            val entityId = buffer.readInt()
            return ClientboundOpenHorseScreenPacket(windowId, columnsCount, entityId)
        }
    }
}