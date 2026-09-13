/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.game.player.PlayerAbilities

public data class ServerboundPlayerAbilitiesPacket(val flags: PlayerAbilities) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundPlayerAbilitiesPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundPlayerAbilitiesPacket) {
            buffer.writeByte(value.flags.mask())
        }

        override fun decode(buffer: BytesBuffer): ServerboundPlayerAbilitiesPacket =
            throw UnsupportedOperationException()
    }
}