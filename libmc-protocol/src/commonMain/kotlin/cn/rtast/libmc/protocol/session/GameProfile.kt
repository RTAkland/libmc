/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.session

import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readMcString
import cn.rtast.libmc.primitives.readUuid
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.writeMcString
import cn.rtast.libmc.primitives.writeUuid
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.stream.BytesBuffer
import kotlin.uuid.Uuid

public data class GameProfile(
    val uuid: Uuid,
    val username: String,
    val properties: List<Property>,
) {
    public data class Property(
        val name: String,
        val value: String,
        val signature: String?,
    ) {
        internal companion object Codec : PacketCodec<Property> {
            override suspend fun encode(buffer: BytesBuffer, value: Property) {
                buffer.writeMcString(value.name)
                buffer.writeMcString(value.value)
                buffer.writeBoolean(value.signature != null)
                value.signature?.let { buffer.writeMcString(it) }
            }

            override suspend fun decode(buffer: BytesBuffer): Property {
                val name = buffer.readMcString()
                val value = buffer.readMcString()
                val hasSignature = buffer.readBoolean()
                val signature = if (hasSignature) buffer.readMcString() else null
                return Property(name, value, signature)
            }
        }
    }

    internal companion object Codec : PacketCodec<GameProfile> {
        override suspend fun encode(buffer: BytesBuffer, value: GameProfile) {
            buffer.writeUuid(value.uuid)
            buffer.writeMcString(value.username)
            buffer.writeVarInt(value.properties.size)  // prefixed array
            value.properties.forEach { prop -> Property.encode(buffer, prop) }
        }

        override suspend fun decode(buffer: BytesBuffer): GameProfile {
            val uuid = buffer.readUuid()
            val username = buffer.readMcString()
            val propertyCount = buffer.readVarInt()
            val properties = List(propertyCount) { Property.decode(buffer) }
            return GameProfile(uuid, username, properties)
        }
    }
}