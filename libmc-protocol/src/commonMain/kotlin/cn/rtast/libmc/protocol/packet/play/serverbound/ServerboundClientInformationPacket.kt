/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.serverbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.writeMcString
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.ChatMode
import cn.rtast.libmc.protocol.protocol.game.player.MainHand
import cn.rtast.libmc.protocol.protocol.game.player.skin.ParticleStatus
import cn.rtast.libmc.protocol.protocol.game.player.skin.SkinPartFlag
import cn.rtast.libmc.protocol.protocol.game.player.skin.SkinPartFlags

public data class ServerboundClientInformationPacket(
    val locale: String,
    /**
     * real type is [Byte]
     */
    val viewDistance: Int,
    val chatMode: ChatMode,
    /**
     * “Colors�?multiplayer setting. The vanilla server stores this value but does nothing with it (see MC-64867).
     * Some third-party servers disable all coloring in chat and system messages when it is false.
     */
    val chatColors: Boolean,
    /**
     * see [SkinPartFlags]
     */
    val displayedSkinParts: SkinPartFlag,
    val mainHand: MainHand,
    /**
     * Enables filtering of text on signs and written book titles.
     * The vanilla client sets this according to the profanityFilterPreferences.profanityFilterOn
     * account attribute indicated by the Mojang API endpoint for player attributes.
     * In offline mode, it is always false.
     */
    val enableTextFiltering: Boolean,
    /**
     * Servers usually list online players; this option should let you not show up in that list.
     */
    val allowServerListings: Boolean,
    val particleStatus: ParticleStatus,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ServerboundClientInformationPacket> {
        override fun encode(buffer: BytesBuffer, value: ServerboundClientInformationPacket) {
            buffer.writeMcString(value.locale)
            buffer.writeByte(value.viewDistance.toByte())
            buffer.writeVarInt(value.chatMode.id)
            buffer.writeBoolean(value.chatColors)
            buffer.writeByte(value.displayedSkinParts.toByte())
            buffer.writeVarInt(value.mainHand.id)
            buffer.writeBoolean(value.enableTextFiltering)
            buffer.writeBoolean(value.allowServerListings)
            buffer.writeVarInt(value.particleStatus.id)
        }

        override fun decode(buffer: BytesBuffer): ServerboundClientInformationPacket =
            throw UnsupportedOperationException()
    }
}