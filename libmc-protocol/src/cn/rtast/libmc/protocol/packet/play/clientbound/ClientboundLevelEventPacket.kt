/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.readBlockPos

public data class ClientboundLevelEventPacket(
    /**
     * see https://minecraft.wiki/w/Java_Edition_protocol/Packets#World_Event
     */
    val eventId: Int,
    val location: BlockPos,
    val data: Int,
    val disableRelativeVolume: Boolean,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundLevelEventPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundLevelEventPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundLevelEventPacket {
            val eventId = buffer.readInt()
            val location = buffer.readBlockPos()
            val data = buffer.readInt()
            val disableRelativeVolume = buffer.readBoolean()
            return ClientboundLevelEventPacket(eventId, location, data, disableRelativeVolume)
        }
    }
}