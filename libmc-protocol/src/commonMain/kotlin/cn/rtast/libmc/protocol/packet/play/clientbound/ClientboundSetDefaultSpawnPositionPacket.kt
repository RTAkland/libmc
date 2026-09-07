/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.readBlockPos
import cn.rtast.libmc.protocol.protocol.game.readIdentifier

public data class ClientboundSetDefaultSpawnPositionPacket(
    val dimensionName: Identifier,
    val location: BlockPos,
    val yaw: Float,
    val pitch: Float,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetDefaultSpawnPositionPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetDefaultSpawnPositionPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetDefaultSpawnPositionPacket {
            val dimensionName = buffer.readIdentifier()
            val location = buffer.readBlockPos()
            val yaw = buffer.readFloat()
            val pitch = buffer.readFloat()
            return ClientboundSetDefaultSpawnPositionPacket(dimensionName, location, yaw, pitch)
        }
    }
}