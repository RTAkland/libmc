/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

/**
 * Used to visually update whether boat paddles are turning.
 * The server will update the Boat entity metadata to match the values here.
 *
 * Right paddle turning is set to true when the left button or forward button is held,
 * left paddle turning is set to true when the right button or forward button is held.
 *
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Paddle_Boat
 */
public data class ServerboundPaddleBoatPacket(
    val leftPaddleTurning: Boolean,
    val rightPaddleTurning: Boolean,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundPaddleBoatPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundPaddleBoatPacket) {
            buffer.writeBoolean(value.leftPaddleTurning)
            buffer.writeBoolean(value.rightPaddleTurning)
        }

        override fun decode(buffer: BytesBuffer): ServerboundPaddleBoatPacket =
            throw UnsupportedOperationException()
    }
}