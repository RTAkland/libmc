/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.entity.attributes.AttributeModifierData
import cn.rtast.libmc.protocol.protocol.game.entity.attributes.AttributeModifierOperation
import cn.rtast.libmc.protocol.protocol.game.entity.attributes.EntityAttributeProperty
import cn.rtast.libmc.protocol.protocol.game.readIdentifier

public data class ClientboundUpdateAttributesPacket(
    val entityId: Int,
    val properties: List<EntityAttributeProperty>,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundUpdateAttributesPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundUpdateAttributesPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundUpdateAttributesPacket {
            val entityId = buffer.readVarInt()
            val properties = buffer.readPrefixed {
                val attributeId = readVarInt()
                val value = readDouble()
                val modifiers = readPrefixed {
                    AttributeModifierData(readIdentifier(), readDouble(), AttributeModifierOperation.fromID(readByte()))
                }
                EntityAttributeProperty(
                    attributeId = attributeId,
                    value = value,
                    modifiers = modifiers
                )
            }
            return ClientboundUpdateAttributesPacket(entityId, properties)
        }
    }
}