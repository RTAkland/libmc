/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.nbt

public fun NBTTag.toJsonString(): String = when (this) {
    is NBTTag.StringTag -> buildString {
        append('"')
        for (ch in value) {
            when (ch) {
                '\\' -> append("\\\\")
                '"' -> append("\\\"")
                '\b' -> append("\\b")
                '\u000C' -> append("\\f")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> {
                    if (ch < ' ') {
                        val hex = ch.code.toString(16).padStart(4, '0')
                        append("\\u").append(hex)
                    } else {
                        append(ch)
                    }
                }
            }
        }
        append('"')
    }

    is NBTTag.ByteTag -> if (value == 1.toByte() || value == 0.toByte()) {
        if (value == 1.toByte()) "true" else "false"
    } else value.toString()

    is NBTTag.ShortTag -> value.toString()
    is NBTTag.IntTag -> value.toString()
    is NBTTag.LongTag -> value.toString()
    is NBTTag.FloatTag -> value.toString()
    is NBTTag.DoubleTag -> value.toString()

    is NBTTag.ByteArrayTag -> value.joinToString(prefix = "[", postfix = "]") { it.toString() }
    is NBTTag.IntArrayTag -> value.joinToString(prefix = "[", postfix = "]") { it.toString() }
    is NBTTag.LongArrayTag -> value.joinToString(prefix = "[", postfix = "]") { it.toString() }

    is NBTTag.ListTag -> value.joinToString(prefix = "[", postfix = "]") { it.toJsonString() }

    is NBTTag.CompoundTag -> value.entries.joinToString(prefix = "{", postfix = "}") { (k, v) ->
        val escapedKey = NBTTag.StringTag(k).toJsonString()
        "$escapedKey:${v.toJsonString()}"
    }
}