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
import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound

public data class ClientboundPlayerCombatDeathPacket(val playerId: Int, val message: NBTCompound) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundPlayerCombatDeathPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundPlayerCombatDeathPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundPlayerCombatDeathPacket {
            val playerId = buffer.readVarInt()
            val message = buffer.readNetworkNBTCompound()
            return ClientboundPlayerCombatDeathPacket(playerId, message)
        }
    }
}