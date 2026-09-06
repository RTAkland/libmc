/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data class ClientboundSetTitleAnimationTimesPacket(val fadeIn: Int, val stay: Int, val fadeOut: Int) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundSetTitleAnimationTimesPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundSetTitleAnimationTimesPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundSetTitleAnimationTimesPacket {
            val fadeIn = buffer.readInt()
            val stay = buffer.readInt()
            val fadeOut = buffer.readInt()
            return ClientboundSetTitleAnimationTimesPacket(fadeIn, stay, fadeOut)
        }
    }
}