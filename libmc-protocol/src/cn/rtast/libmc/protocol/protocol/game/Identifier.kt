/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.protocol.game

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readMcString
import cn.rtast.libmc.primitives.writeMcString
import kotlin.jvm.JvmInline

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Identifier
 */
@JvmInline
public value class Identifier internal constructor(public val raw: String) {
    public val namespace: String get() = if (raw.contains(':')) raw.substringBefore(':') else "minecraft"
    public val path: String get() = if (raw.contains(':')) raw.substringAfter(':') else raw

    override fun toString(): String = "$namespace:$path"

    public companion object Codec : PacketCodec<Identifier> {
        public fun of(namespace: String, path: String): Identifier = Identifier("$namespace:$path")
        public fun of(full: String): Identifier = Identifier(full)

        override fun encode(buffer: BytesBuffer, value: Identifier) {
            buffer.writeMcString(value.toString())
        }

        override fun decode(buffer: BytesBuffer): Identifier = Identifier(buffer.readMcString())
    }
}

internal fun BytesBuffer.readIdentifier(): Identifier = Identifier.decode(this)
internal fun BytesBuffer.writeIdentifier(identifier: Identifier) = Identifier.encode(this, identifier)