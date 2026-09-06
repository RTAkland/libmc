/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.protocol.game

import cn.rtast.libmc.common.BytesBuffer
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common.readMcString
import cn.rtast.libmc.common.writeMcString
import kotlin.jvm.JvmInline

/**
 * ref: https://minecraft.wiki/w/Java_Edition_protocol/Packets#Identifier
 */
@JvmInline
public value class Identifier internal constructor(public val raw: String) {
    public val namespace: String get() = if (raw.contains(':')) raw.substringBefore(':') else "minecraft"
    public val path: String get() = if (raw.contains(':')) raw.substringAfter(':') else raw

    override fun toString(): String = "$namespace:$path"

    internal companion object Codec : PacketCodec<Identifier> {
        fun of(namespace: String, path: String): Identifier = Identifier("$namespace:$path")
        fun of(full: String): Identifier = Identifier(full)

        override fun encode(buffer: BytesBuffer, value: Identifier) {
            buffer.writeMcString(value.toString())
        }

        override fun decode(buffer: BytesBuffer): Identifier = Identifier(buffer.readMcString())
    }
}

internal fun BytesBuffer.readIdentifier(): Identifier = Identifier.decode(this)
internal fun BytesBuffer.writeIdentifier(identifier: Identifier) = Identifier.encode(this, identifier)