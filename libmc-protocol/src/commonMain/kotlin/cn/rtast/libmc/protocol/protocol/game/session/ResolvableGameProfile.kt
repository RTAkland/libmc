/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/9
 */


package cn.rtast.libmc.protocol.protocol.game.session

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.*
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.player.skin.SkinModelType
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier
import kotlin.uuid.Uuid

public data class ResolvableGameProfile(
    val profile: UnpackedProfile,
    val body: Identifier?,
    val cape: Identifier?,
    val elytra: Identifier?,
    val model: SkinModelType?,
) {
    public sealed interface UnpackedProfile {
        public data class PartialProfile(
            val username: String?,
            val uuid: Uuid?,
            val properties: GameProfile.Property,
        ) : UnpackedProfile

        public data class CompleteProfile(val profile: GameProfile) : UnpackedProfile
    }
}

internal fun BytesBuffer.readResolvableProfile(): ResolvableGameProfile {
    val unpackedProfile = when (val type = readVarInt()) {
        0 -> {
            val username = readPrefixOptional { readMcString() }
            val uuid = readPrefixOptional { readUuid() }
            val properties = GameProfile.Property.decode(this)
            ResolvableGameProfile.UnpackedProfile.PartialProfile(username, uuid, properties)
        }

        1 -> ResolvableGameProfile.UnpackedProfile.CompleteProfile(GameProfile.decode(this))
        else -> error("Unknown profile type $type")
    }
    val body = readPrefixOptional { readIdentifier() }
    val cape = readPrefixOptional { readIdentifier() }
    val elytra = readPrefixOptional { readIdentifier() }
    val model = readPrefixOptional { SkinModelType.fromID(readVarInt()) }
    return ResolvableGameProfile(unpackedProfile, body, cape, elytra, model)
}

internal fun BytesBuffer.writeResolvableProfile(profile: ResolvableGameProfile) {
    when (profile.profile) {
        is ResolvableGameProfile.UnpackedProfile.CompleteProfile -> {
            writeVarInt(1)
            writePrefixedOptional(profile.profile.profile) {
                GameProfile.encode(this, it)
            }
        }

        is ResolvableGameProfile.UnpackedProfile.PartialProfile -> {
            writeVarInt(0)
            writePrefixedOptional(profile.profile.username) { writeMcString(it) }
            writePrefixedOptional(profile.profile.uuid) { writeUuid(it) }
            writePrefixedOptional(profile.profile.properties) { GameProfile.Property.encode(this, it) }
        }
    }
    writePrefixedOptional(profile.body) { writeIdentifier(it) }
    writePrefixedOptional(profile.cape) { writeIdentifier(it) }
    writePrefixedOptional(profile.elytra) { writeIdentifier(it) }
    writePrefixedOptional(profile.model) { writeVarInt(it.id) }
}