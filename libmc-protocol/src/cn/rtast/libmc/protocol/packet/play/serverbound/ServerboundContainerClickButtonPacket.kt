/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.block.EnchantmentButton
import cn.rtast.libmc.protocol.protocol.game.block.LecternButton

public data class ServerboundContainerClickButtonPacket(val windowId: Int, val buttonId: Int) : MinecraftPacket {
    public constructor(windowId: Int, button: EnchantmentButton) : this(windowId, button.id)
    public constructor(windowId: Int, button: LecternButton) : this(windowId, button.id)

    internal companion object Codec : PacketCodec<ServerboundContainerClickButtonPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundContainerClickButtonPacket) {
            buffer.writeVarInt(value.windowId)
            buffer.writeVarInt(value.buttonId)
        }

        override fun decode(buffer: BytesBuffer): ServerboundContainerClickButtonPacket =
            throw UnsupportedOperationException()
    }
}