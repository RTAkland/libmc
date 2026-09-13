/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readPrefixOptional
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.advancement.Advancement
import cn.rtast.libmc.protocol.protocol.game.advancement.AdvancementProgress
import cn.rtast.libmc.protocol.protocol.game.advancement.readAdvancement
import cn.rtast.libmc.protocol.protocol.game.readIdentifier

public data class ClientboundUpdateAdvancementsPacket(
    val reset: Boolean,
    val advancementMapping: Map<Identifier, Advancement>,
    /**
     * The identifiers of the advancements that should be removed
     */
    val identifiers: List<Identifier>,
    val progressMapping: Map<Identifier, AdvancementProgress>,
    val showAdvancements: Boolean,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundUpdateAdvancementsPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundUpdateAdvancementsPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundUpdateAdvancementsPacket {
            val reset = buffer.readBoolean()
            val mappingCount = buffer.readVarInt()
            val advancementMapping = HashMap<Identifier, Advancement>(mappingCount)
            repeat(mappingCount) {
                val key = buffer.readIdentifier()
                val advancement = buffer.readAdvancement()
                advancementMapping[key] = advancement
            }
            val identifiers = buffer.readPrefixed { readIdentifier() }
            val progressMappingCount = buffer.readVarInt()
            val progressMapping = HashMap<Identifier, AdvancementProgress>(progressMappingCount)
            repeat(progressMappingCount) {
                val key = buffer.readIdentifier()
                val criteriaCount = buffer.readVarInt()
                val criteria = HashMap<Identifier, Long?>(criteriaCount)
                repeat(criteriaCount) {
                    val k = buffer.readIdentifier()
                    val date = buffer.readPrefixOptional { readLong() }
                    criteria[k] = date
                }
                progressMapping[key] = AdvancementProgress(criteria)
            }
            val showAdvancements = buffer.readBoolean()
            return ClientboundUpdateAdvancementsPacket(
                reset, advancementMapping, identifiers,
                progressMapping, showAdvancements
            )
        }
    }
}