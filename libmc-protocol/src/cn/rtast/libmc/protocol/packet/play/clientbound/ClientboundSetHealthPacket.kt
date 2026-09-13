/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.readVarInt

public data class ClientboundSetHealthPacket(
    /**
     * 0 or less = dead, 20 = full HP.
     */
    val health: Float,
    /**
     * 0 ~ 20
     */
    val food: Int,
    /**
     * Seems to vary from 0.0 to 5.0 in integer increments.
     */
    val foodSaturation: Float,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetHealthPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetHealthPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetHealthPacket {
            val health = buffer.readFloat()
            val food = buffer.readVarInt()
            val foodSaturation = buffer.readFloat()
            return ClientboundSetHealthPacket(health, food, foodSaturation)
        }
    }
}