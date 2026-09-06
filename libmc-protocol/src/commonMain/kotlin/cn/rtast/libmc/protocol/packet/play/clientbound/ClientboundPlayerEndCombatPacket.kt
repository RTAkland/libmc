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
import cn.rtast.libmc.protocol.tick.ticks
import kotlin.time.Duration

public data class ClientboundPlayerEndCombatPacket(val duration: Duration) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundPlayerEndCombatPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundPlayerEndCombatPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundPlayerEndCombatPacket {
            return ClientboundPlayerEndCombatPacket(buffer.readVarInt().ticks)  // convert int to minecraft ticks
        }
    }
}