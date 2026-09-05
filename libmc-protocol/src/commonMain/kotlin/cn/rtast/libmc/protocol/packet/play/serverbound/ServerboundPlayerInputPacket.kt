/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.packet.MinecraftPacket

public data class ServerboundPlayerInputPacket(
    val forward: Boolean,
    val backward: Boolean,
    val left: Boolean,
    val right: Boolean,
    val jump: Boolean,
    val sneak: Boolean,
    val sprint: Boolean,
) : MinecraftPacket {
    public companion object Codec : PacketCodec<ServerboundPlayerInputPacket> {
        private const val FLAG_FORWARD = 0x01
        private const val FLAG_BACKWARD = 0x02
        private const val FLAG_LEFT = 0x04
        private const val FLAG_RIGHT = 0x08
        private const val FLAG_JUMP = 0x10
        private const val FLAG_SNEAK = 0x20
        private const val FLAG_SPRINT = 0x40

        override fun encode(buffer: BytesBuffer, value: ServerboundPlayerInputPacket) {
            var flags = 0
            if (value.forward) flags = flags or FLAG_FORWARD
            if (value.backward) flags = flags or FLAG_BACKWARD
            if (value.left) flags = flags or FLAG_LEFT
            if (value.right) flags = flags or FLAG_RIGHT
            if (value.jump) flags = flags or FLAG_JUMP
            if (value.sneak) flags = flags or FLAG_SNEAK
            if (value.sprint) flags = flags or FLAG_SPRINT
            buffer.writeByte(flags.toByte())
        }

        override fun decode(buffer: BytesBuffer): ServerboundPlayerInputPacket =
            throw UnsupportedOperationException()
    }
}