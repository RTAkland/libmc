/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/8
 */


package cn.rtast.libmc.snbt

import cn.rtast.libmc.nbt.NBTTag

public fun NBTTag.toSNBT(prettyPrint: Boolean = false, indentLevel: Int = 0): String = when (this) {
    is NBTTag.ByteTag -> "${value}b"
    is NBTTag.ShortTag -> "${value}s"
    is NBTTag.IntTag -> "$value"
    is NBTTag.LongTag -> "${value}L"
    is NBTTag.FloatTag -> "${value}f"
    is NBTTag.DoubleTag -> "${value}d"
    is NBTTag.StringTag -> escapeSNBTString(value)
    is NBTTag.ByteArrayTag -> value.joinToString(prefix = "[B; ", postfix = "]", separator = ", ") { "${it}b" }
    is NBTTag.IntArrayTag -> value.joinToString(prefix = "[I; ", postfix = "]", separator = ", ") { "$it" }
    is NBTTag.LongArrayTag -> value.joinToString(prefix = "[L; ", postfix = "]", separator = ", ") { "${it}L" }
    is NBTTag.ListTag -> {
        if (value.isEmpty()) "[]" else if (!prettyPrint) {
            value.joinToString(prefix = "[", postfix = "]", separator = ",") { it.toSNBT(false) }
        } else {
            val indent = "  ".repeat(indentLevel + 1)
            val closingIndent = "  ".repeat(indentLevel)
            val body = value.joinToString(separator = ",\n") { "$indent${it.toSNBT(true, indentLevel + 1)}" }
            "[\n$body\n$closingIndent]"
        }
    }

    is NBTTag.CompoundTag -> {
        if (value.isEmpty()) "{}" else if (!prettyPrint) {
            value.entries.joinToString(prefix = "{", postfix = "}", separator = ",") { (k, v) ->
                "${escapeSNBTKey(k)}:${v.toSNBT(false)}"
            }
        } else {
            val indent = "  ".repeat(indentLevel + 1)
            val closingIndent = "  ".repeat(indentLevel)
            val body = value.entries.joinToString(separator = ",\n") { (k, v) ->
                "$indent${escapeSNBTKey(k)}: ${v.toSNBT(true, indentLevel + 1)}"
            }
            "{\n$body\n$closingIndent}"
        }
    }
}

internal fun escapeSNBTKey(key: String): String {
    if (key.isEmpty()) return "\"\""
    val first = key.first()
    val isValidFirst = first in 'a'..'z' || first in 'A'..'Z' || first == '_'
    val isValidBody =
        key.all { it in 'a'..'z' || it in 'A'..'Z' || it in '0'..'9' || it == '_' || it == '-' || it == '+' || it == '.' }
    return if (isValidFirst && isValidBody) key else escapeSNBTString(key)
}

internal fun escapeSNBTString(str: String): String {
    val sb = StringBuilder("\"")
    for (ch in str) {
        when (ch) {
            '\\' -> sb.append("\\\\")
            '"' -> sb.append("\\\"")
            '\b' -> sb.append("\\b")
            '\u000C' -> sb.append("\\f")
            '\n' -> sb.append("\\n")
            '\r' -> sb.append("\\r")
            '\t' -> sb.append("\\t")
            else -> if (ch.code < 0x20) sb.append("\\x${ch.code.toString(16).padStart(2, '0')}") else sb.append(ch)
        }
    }
    sb.append("\"")
    return sb.toString()
}