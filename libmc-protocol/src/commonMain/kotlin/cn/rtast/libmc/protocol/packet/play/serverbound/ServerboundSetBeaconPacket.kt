/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket
import cn.rtast.libmc.common.writeVarInt

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Set_Beacon_Effect
 */
public data class ServerboundSetBeaconPacket(val primaryEffect: Int?, val secondaryEffect: Int?) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundSetBeaconPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundSetBeaconPacket) {
            buffer.writeBoolean(value.primaryEffect != null)
            if (value.primaryEffect != null) buffer.writeVarInt(value.primaryEffect)
            buffer.writeBoolean(value.secondaryEffect != null)
            if (value.secondaryEffect != null) buffer.writeVarInt(value.secondaryEffect)
        }

        override fun decode(buffer: BytesBuffer): ServerboundSetBeaconPacket =
            throw UnsupportedOperationException()
    }
}