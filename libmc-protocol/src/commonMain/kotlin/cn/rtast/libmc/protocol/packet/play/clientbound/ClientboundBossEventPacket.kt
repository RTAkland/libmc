/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readUuid
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.protocol.protocol.game.bossbar.BossBarAction
import cn.rtast.libmc.protocol.protocol.game.bossbar.BossBarColor
import cn.rtast.libmc.protocol.protocol.game.bossbar.BossBarDivision
import cn.rtast.libmc.protocol.protocol.game.bossbar.BossBarFlags
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound
import kotlin.uuid.Uuid

public data class ClientboundBossEventPacket(val uuid: Uuid, val action: BossBarAction) : ClientboundPlayPacket {
    internal companion object Codec : PacketCodec<ClientboundBossEventPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundBossEventPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundBossEventPacket {
            val uuid = buffer.readUuid()
            val action = when (val actionId = buffer.readVarInt()) {
                BossBarAction.ADD_ID -> {
                    val title = buffer.readNetworkNBTCompound()
                    val health = buffer.readFloat()
                    val color = BossBarColor.fromID(buffer.readVarInt())
                    val division = BossBarDivision.fromID(buffer.readVarInt())
                    val flags = BossBarFlags.fromBitmask(buffer.readByte().toInt() and 0xFF)
                    BossBarAction.Add(title, health, color, division, flags)
                }

                BossBarAction.REMOVE_ID -> BossBarAction.Remove
                BossBarAction.UPDATE_TITLE_ID -> BossBarAction.UpdateTitle(buffer.readNetworkNBTCompound())
                BossBarAction.UPDATE_STYLE_ID -> {
                    val color = BossBarColor.fromID(buffer.readVarInt())
                    val division = BossBarDivision.fromID(buffer.readVarInt())
                    BossBarAction.UpdateStyle(color, division)
                }

                BossBarAction.UPDATE_FLAGS_ID -> {
                    BossBarAction.UpdateFlags(BossBarFlags.fromBitmask(buffer.readByte().toInt() and 0xFF))
                }

                else -> throw IllegalArgumentException("Unknown action $actionId")
            }
            return ClientboundBossEventPacket(uuid, action)
        }
    }
}