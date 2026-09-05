/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.protocol.protocol.game.*
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.readBlockPos

public data class ClientboundLoginPlayPacket(
    val entityId: Int,
    val isHardcore: Boolean,
    val dimensionNames: List<Identifier>,
    val maxPlayers: Int,
    val viewDistance: Int,
    val simulationDistance: Int,
    val isReducedDebugInfo: Boolean,
    val enableRespawnScreen: Boolean,
    val doLimitedCrafting: Boolean,
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
    val isOnlineMode: Boolean,
    val enforceSecureChat: Boolean,
) : ClientboundPlayPacket {
    public companion object Codec : PacketCodec<ClientboundLoginPlayPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundLoginPlayPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundLoginPlayPacket {
            val entityId = buffer.readInt()
            val isHardcore = buffer.readBoolean()
            val dimensionNamesCount = buffer.readVarInt()
            val dimensionNames = List(dimensionNamesCount) { buffer.readIdentifier() }
            val maxPlayers = buffer.readVarInt()
            val viewDistance = buffer.readVarInt()
            val simulationDistance = buffer.readVarInt()
            val isReducedDebugInfo = buffer.readBoolean()
            val enableRespawnScreen = buffer.readBoolean()
            val doLimitedCrafting = buffer.readBoolean()
            val dimensionType = buffer.readVarInt()
            val dimensionName = buffer.readIdentifier()
            val hashedSeed = buffer.readLong()
            val gameMode = GameMode.fromID(buffer.readByte().toUByte())
            val previousGameMode = GameMode.fromID(buffer.readByte())
            val isDebug = buffer.readBoolean()
            val isFlat = buffer.readBoolean()
            val hasDeathLocation = buffer.readBoolean()
            val deathDimensionName = if (hasDeathLocation) buffer.readIdentifier() else null
            val deathLocation = if (hasDeathLocation) buffer.readBlockPos() else null
            val portalCooldown = buffer.readVarInt()
            val seaLevel = buffer.readVarInt()
            val isOnlineMode = buffer.readBoolean()
            val isEnforcesSecureChat = buffer.readBoolean()
            return ClientboundLoginPlayPacket(
                entityId, isHardcore, dimensionNames, maxPlayers,
                viewDistance, simulationDistance, isReducedDebugInfo,
                enableRespawnScreen, doLimitedCrafting, dimensionType,
                dimensionName, hashedSeed, gameMode, previousGameMode,
                isDebug, isFlat, hasDeathLocation, deathDimensionName,
                deathLocation, portalCooldown, seaLevel, isOnlineMode,
                isEnforcesSecureChat
            )
        }
    }
}