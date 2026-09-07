/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.readOptional
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.GameMode
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.readBlockPos
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.world.RespawnDataToKeep

public data class ClientboundRespawnPacket(
    val dimensionType: Int,
    val dimensionName: Identifier,
    val hashedSeed: Long,
    val gameMode: GameMode,
    val previousGameMode: GameMode,
    val isDebug: Boolean,
    val isFlat: Boolean,
    val hasDeathLocation: Boolean,
    val deathDimensionName: Identifier?,
    val deathLocation: BlockPos?,
    val portalCooldown: Int,
    val seaLevel: Int,
    val dataKept: RespawnDataToKeep,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundRespawnPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundRespawnPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundRespawnPacket {
            val dimensionType = buffer.readVarInt()
            val dimensionName = buffer.readIdentifier()
            val hashedSeed = buffer.readLong()
            val gameMode = GameMode.fromID(buffer.readUByte())
            val previousGameMode = GameMode.fromID(buffer.readByte())
            val isDebug = buffer.readBoolean()
            val isFlat = buffer.readBoolean()
            val hasDeathLocation = buffer.readBoolean()
            val deathDimensionName = buffer.readOptional { readIdentifier() }
            val deathLocation = buffer.readOptional { readBlockPos() }
            val portalCooldown = buffer.readVarInt()
            val seaLevel = buffer.readVarInt()
            val dataKept = RespawnDataToKeep.fromByte(buffer.readByte())
            return ClientboundRespawnPacket(
                dimensionType, dimensionName, hashedSeed,
                gameMode, previousGameMode, isDebug,
                isFlat, hasDeathLocation,
                deathDimensionName, deathLocation,
                portalCooldown, seaLevel, dataKept
            )
        }
    }
}