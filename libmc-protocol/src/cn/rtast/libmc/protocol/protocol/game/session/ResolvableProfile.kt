/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.session

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.*
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.player.skin.TextureModelType
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier
import kotlin.uuid.Uuid

public data class ResolvableProfile(
    val profile: UnpackedProfile,
    val body: Identifier?,
    val cape: Identifier?,
    val elytra: Identifier?,
    val model: TextureModelType?,
) {
    public sealed interface UnpackedProfile {
        public data class PartialProfile(
            val username: String?,
            val uuid: Uuid?,
            val properties: List<GameProfile.Property>,
        ) : UnpackedProfile

        public data class CompleteProfile(val profile: GameProfile) : UnpackedProfile
    }
}

internal fun BytesBuffer.readResolvableProfile(): ResolvableProfile {
    val kind = readVarInt()
    val profile = if (kind == 0) {
        val username = readPrefixOptional { readMcString() }
        val uuid = readPrefixOptional { readUuid() }
        val properties = readPrefixed { GameProfile.Property.decode(this) }
        ResolvableProfile.UnpackedProfile.PartialProfile(username, uuid, properties)
    } else ResolvableProfile.UnpackedProfile.CompleteProfile(GameProfile.decode(this))
    val body = readPrefixOptional { readIdentifier() }
    val cape = readPrefixOptional { readIdentifier() }
    val elytra = readPrefixOptional { readIdentifier() }
    val model = readPrefixOptional { TextureModelType.fromID(readVarInt()) }
    return ResolvableProfile(profile, body, cape, elytra, model)
}

internal fun BytesBuffer.writeResolvableProfile(profile: ResolvableProfile) {
    when (profile.profile) {
        is ResolvableProfile.UnpackedProfile.CompleteProfile -> {
            writeVarInt(1)
            GameProfile.encode(this, profile.profile.profile)
        }

        is ResolvableProfile.UnpackedProfile.PartialProfile -> {
            writeVarInt(0)
            writePrefixedOptional(profile.profile.username) { writeMcString(it) }
            writePrefixedOptional(profile.profile.uuid) { writeUuid(it) }
            writePrefixed(profile.profile.properties) { GameProfile.Property.encode(this, it) }
        }
    }
    writePrefixedOptional(profile.body) { writeIdentifier(it) }
    writePrefixedOptional(profile.cape) { writeIdentifier(it) }
    writePrefixedOptional(profile.elytra) { writeIdentifier(it) }
    writePrefixedOptional(profile.model) { writeVarInt(it.id) }
}