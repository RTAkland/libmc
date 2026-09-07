/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.stream.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.readMcString
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.scoreboard.ObjectiveDisplayPosition

public data class ClientboundSetDisplayObjectivePacket(val position: ObjectiveDisplayPosition, val scoreName: String) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetDisplayObjectivePacket> {
        override suspend fun encode(buffer: BytesBuffer, value: ClientboundSetDisplayObjectivePacket) {}
        override suspend fun decode(buffer: BytesBuffer): ClientboundSetDisplayObjectivePacket {
            val position = ObjectiveDisplayPosition.fromID(buffer.readVarInt())
            val scoreName = buffer.readMcString()
            return ClientboundSetDisplayObjectivePacket(position, scoreName)
        }
    }
}