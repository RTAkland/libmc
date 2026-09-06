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
import cn.rtast.libmc.protocol.protocol.game.math.Vec3d
import cn.rtast.libmc.protocol.protocol.game.math.readVec3d
import cn.rtast.libmc.protocol.protocol.game.player.AnchorPoint

public data class ClientboundPlayerLookAtPacket(
    val fromAnchor: AnchorPoint,
    val targetPosition: Vec3d,
    val entityTarget: EntityLookAtTarget?,
) : MinecraftPacket {
    public data class EntityLookAtTarget(
        val entityId: Int,
        val entityAnchor: AnchorPoint,
    )

    internal companion object Codec : PacketCodec<ClientboundPlayerLookAtPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundPlayerLookAtPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundPlayerLookAtPacket {
            val fromAnchor = AnchorPoint.fromID(buffer.readVarInt())
            val targetPosition = buffer.readVec3d()!!
            val isEntity = buffer.readBoolean()
            val entityTarget = if (isEntity) {
                val entityId = buffer.readVarInt()
                val entityAnchor = AnchorPoint.fromID(buffer.readVarInt())
                EntityLookAtTarget(entityId, entityAnchor)
            } else null
            return ClientboundPlayerLookAtPacket(
                fromAnchor = fromAnchor,
                targetPosition = targetPosition,
                entityTarget = entityTarget
            )
        }
    }
}