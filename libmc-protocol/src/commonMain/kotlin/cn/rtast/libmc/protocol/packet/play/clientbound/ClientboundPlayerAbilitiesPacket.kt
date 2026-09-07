/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.game.player.PlayerAbilities

public data class ClientboundPlayerAbilitiesPacket(
    val flags: PlayerAbilities,
    val flyingSpeed: Float,
    val fovModifier: Float,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundPlayerAbilitiesPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundPlayerAbilitiesPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundPlayerAbilitiesPacket {
            val flag = PlayerAbilities.fromByte(buffer.readByte())
            val flyingSpeed = buffer.readFloat()
            val fovModifier = buffer.readFloat()
            return ClientboundPlayerAbilitiesPacket(flag, flyingSpeed, fovModifier)
        }
    }
}