/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readVarInt
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.readBlockPos

public data class ClientboundBlockEventPacket(
    val location: BlockPos,
    /**
     * ref: https://minecraft.wiki/w/Java_Edition_protocol/Block_actions
     */
    val actionId: UByte,
    /**
     * ref: https://minecraft.wiki/w/Java_Edition_protocol/Block_actions
     */
    val actionParameter: UByte,
    /**
     * ID in the minecraft:block registry.
     * This value is unused by the vanilla client,
     * as it will infer the type of block based on the given position.
     */
    val blockType: Int,
) : ClientboundPlayPacket {
    internal companion object Codec : PacketCodec<ClientboundBlockEventPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundBlockEventPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundBlockEventPacket {
            val location = buffer.readBlockPos()
            val actionId = buffer.readUByte()
            val actionParameter = buffer.readUByte()
            val blockType = buffer.readVarInt()
            return ClientboundBlockEventPacket(location, actionId, actionParameter, blockType)
        }
    }
}