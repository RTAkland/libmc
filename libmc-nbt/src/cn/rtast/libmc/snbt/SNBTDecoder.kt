/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/8
 */


package cn.rtast.libmc.snbt

import cn.rtast.libmc.nbt.NBTTag
import cn.rtast.libmc.nbt.NBTType

public class SNBTDecoder internal constructor(private val src: String) {
    private var ptr = 0

    public fun parse(): NBTTag {
        skipWhitespace()
        val result = parseTag()
        skipWhitespace()
        require(ptr >= src.length) { "Unexpected trailing characters at index $ptr: '${src.substring(ptr)}'" }
        return result
    }

    private fun parseTag(): NBTTag {
        skipWhitespace()
        check(ptr < src.length) { "Unexpected end of SNBT string" }
        if (lookingAtOperation()) return parseOperation()
        return when (src[ptr]) {
            '{' -> parseCompoundTag()
            '[' -> parseListOrArrayTag()
            '"', '\'' -> NBTTag.StringTag(parseQuotedString())
            else -> parseNumberOrStringTag()
        }
    }

    private fun parseCompoundTag(): NBTTag.CompoundTag {
        expect('{')
        val map = LinkedHashMap<String, NBTTag>()
        skipWhitespace()
        if (peek() == '}') {
            ptr++
            return NBTTag.CompoundTag(map)
        }
        while (true) {
            skipWhitespace()
            val key = parseKey()
            skipWhitespace()
            expect(':')
            val value = parseTag()
            map[key] = value
            skipWhitespace()
            val ch = peek()
            if (ch == '}') {
                ptr++
                break
            }
            if (ch == ',') {
                ptr++
                skipWhitespace()
                if (peek() == '}') {
                    ptr++
                    break
                }
            } else error("Expected ',' or '}' at index $ptr, found '$ch'")
        }
        return NBTTag.CompoundTag(map)
    }

    private fun parseListOrArrayTag(): NBTTag {
        expect('[')
        skipWhitespace()
        if (ptr + 1 < src.length && src[ptr + 1] == ';') {
            val typeChar = src[ptr].uppercaseChar()
            ptr += 2
            return parseNativeArray(typeChar)
        }
        val list = ArrayList<NBTTag>()
        if (peek() == ']') {
            ptr++
            return NBTTag.ListTag(NBTType.End, list)
        }

        var elementType = NBTType.End
        while (true) {
            val tag = parseTag()
            if (elementType == NBTType.End) elementType = tag.type
            list.add(tag)
            skipWhitespace()
            val ch = peek()
            if (ch == ']') {
                ptr++
                break
            }
            if (ch == ',') {
                ptr++
                skipWhitespace()
                if (peek() == ']') {
                    ptr++
                    break
                }
            } else error("Expected ',' or ']' at index $ptr, found '$ch'")
        }
        return NBTTag.ListTag(elementType, list)
    }

    private fun parseNativeArray(typeChar: Char): NBTTag {
        skipWhitespace()
        val rawTags = mutableListOf<NBTTag>()
        if (peek() == ']') {
            ptr++
            return castNativeArray(typeChar, rawTags)
        }
        while (true) {
            rawTags.add(parseTag())
            skipWhitespace()
            val ch = peek()
            if (ch == ']') {
                ptr++
                break
            }
            if (ch == ',') {
                ptr++
                skipWhitespace()
                if (peek() == ']') {
                    ptr++
                    break
                }
            } else error("Expected ',' or ']' at index $ptr, found '$ch'")
        }
        return castNativeArray(typeChar, rawTags)
    }

    private fun castNativeArray(typeChar: Char, elements: List<NBTTag>): NBTTag = when (typeChar) {
        'B' -> NBTTag.ByteArrayTag(ByteArray(elements.size) { extractNumber(elements[it]).toByte() })
        'I' -> NBTTag.IntArrayTag(IntArray(elements.size) { extractNumber(elements[it]).toInt() })
        'L' -> NBTTag.LongArrayTag(LongArray(elements.size) { extractNumber(elements[it]).toLong() })
        else -> error("Unknown array prefix: $typeChar")
    }

    private fun extractNumber(tag: NBTTag): Number = when (tag) {
        is NBTTag.ByteTag -> tag.value
        is NBTTag.ShortTag -> tag.value
        is NBTTag.IntTag -> tag.value
        is NBTTag.LongTag -> tag.value
        is NBTTag.FloatTag -> tag.value.toInt()
        is NBTTag.DoubleTag -> tag.value.toLong()
        else -> error("Cannot convert $tag to array element")
    }

    private fun parseKey(): String {
        return when (peek()) {
            '"', '\'' -> parseQuotedString()
            else -> parseUnquotedKey()
        }
    }

    private fun parseUnquotedKey(): String {
        val start = ptr
        while (ptr < src.length && isAllowedUnquotedChar(src[ptr])) ptr++
        val key = src.substring(start, ptr)
        require(key.isNotEmpty()) { "Expected key at index $start" }
        return key
    }

    private fun parseNumberOrStringTag(): NBTTag {
        val start = ptr
        while (ptr < src.length && isAllowedUnquotedChar(src[ptr])) ptr++
        val raw = src.substring(start, ptr)
        if (raw.equals("true", ignoreCase = true)) return NBTTag.ByteTag(1)
        if (raw.equals("false", ignoreCase = true)) return NBTTag.ByteTag(0)
        val parsedNum = tryParseNumber(raw)
        if (parsedNum != null) return parsedNum
        val first = raw.firstOrNull()
        if (first in '0'..'9' || first == '.' || first == '+' || first == '-') error("Unquoted string cannot start with '$first': $raw")
        return NBTTag.StringTag(raw)
    }

    private fun tryParseNumber(raw: String): NBTTag? {
        val clean = raw.replace("_", "")
        val lower = clean.lowercase()
        if (lower.endsWith("f")) return NBTTag.FloatTag(lower.dropLast(1).toFloatOrNull() ?: return null)
        if (lower.endsWith("d")) return NBTTag.DoubleTag(lower.dropLast(1).toDoubleOrNull() ?: return null)
        if (clean.contains('.') || lower.contains('e')) return NBTTag.DoubleTag(clean.toDoubleOrNull() ?: return null)
        var rad = 10
        var digits = lower
        if (digits.startsWith("0x")) {
            rad = 16
            digits = digits.substring(2)
        } else if (digits.startsWith("0b")) {
            rad = 2
            digits = digits.substring(2)
        }
        val (numStr, suffix) = splitSuffix(digits)
        return try {
            val unsigned = suffix.startsWith("u") || if (suffix.startsWith("s")) false else rad != 10
            val typeChar = suffix.lastOrNull() ?: if (rad == 10) 'i' else 'i'
            when (typeChar) {
                'b' -> NBTTag.ByteTag(if (unsigned) numStr.toUByte(rad).toByte() else numStr.toByte(rad))
                's' -> NBTTag.ShortTag(if (unsigned) numStr.toUShort(rad).toShort() else numStr.toShort(rad))
                'l' -> NBTTag.LongTag(if (unsigned) numStr.toULong(rad).toLong() else numStr.toLong(rad))
                else -> NBTTag.IntTag(if (unsigned) numStr.toUInt(rad).toInt() else numStr.toInt(rad))
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun splitSuffix(str: String): Pair<String, String> {
        val suffixes = listOf("sb", "ub", "ss", "us", "si", "ui", "sl", "ul", "b", "s", "i", "l")
        for (s in suffixes) if (str.endsWith(s)) return Pair(str.dropLast(s.length), s)
        return Pair(str, "")
    }

    private fun parseQuotedString(): String {
        val quote = src[ptr++]
        val sb = StringBuilder()
        while (ptr < src.length) {
            val ch = src[ptr++]
            if (ch == quote) return sb.toString()
            if (ch == '\\') {
                if (ptr >= src.length) break
                when (val esc = src[ptr++]) {
                    'b' -> sb.append('\b')
                    'f' -> sb.append('\u000C')
                    'n' -> sb.append('\n')
                    'r' -> sb.append('\r')
                    's' -> sb.append(' ')
                    't' -> sb.append('\t')
                    '\\' -> sb.append('\\')
                    '\'', '"' -> sb.append(esc)
                    'x' -> {
                        val hex = src.substring(ptr, ptr + 2)
                        ptr += 2
                        sb.append(hex.toInt(16).toChar())
                    }

                    'u' -> {
                        val hex = src.substring(ptr, ptr + 4)
                        ptr += 4
                        sb.append(hex.toInt(16).toChar())
                    }

                    'U' -> {
                        val hex = src.substring(ptr, ptr + 8)
                        ptr += 8
                        val codePoint = hex.toInt(16)
                        sb.appendCodePoint(codePoint)
                    }

                    else -> sb.append(esc)
                }
            } else sb.append(ch)
        }
        error("Unterminated quoted string")
    }

    private fun lookingAtOperation(): Boolean {
        val rest = src.substring(ptr)
        return rest.startsWith("bool(") || rest.startsWith("uuid(")
    }

    private fun parseOperation(): NBTTag {
        val isBool = src.substring(ptr).startsWith("bool(")
        val opName = if (isBool) "bool" else "uuid"
        ptr += opName.length
        expect('(')
        skipWhitespace()
        val argTag = parseTag()
        skipWhitespace()
        expect(')')
        return if (isBool) {
            val isTrue = when (argTag) {
                is NBTTag.ByteTag -> argTag.value != 0.toByte()
                is NBTTag.ShortTag -> argTag.value != 0.toShort()
                is NBTTag.IntTag -> argTag.value != 0
                is NBTTag.LongTag -> argTag.value != 0L
                is NBTTag.FloatTag -> argTag.value != 0.0f
                is NBTTag.DoubleTag -> argTag.value != 0.0
                else -> error("bool() requires a number or boolean argument")
            }
            NBTTag.ByteTag(if (isTrue) 1 else 0)
        } else {
            require(argTag is NBTTag.StringTag) { "uuid() argument must be a string" }
            val uuidStr = argTag.value.replace("-", "")
            require(uuidStr.length == 32) { "Invalid UUID string: ${argTag.value}" }
            val i1 = uuidStr.substring(0, 8).toLong(16).toInt()
            val i2 = uuidStr.substring(8, 16).toLong(16).toInt()
            val i3 = uuidStr.substring(16, 24).toLong(16).toInt()
            val i4 = uuidStr.substring(24, 32).toLong(16).toInt()
            NBTTag.IntArrayTag(intArrayOf(i1, i2, i3, i4))
        }
    }

    private fun isAllowedUnquotedChar(ch: Char): Boolean =
        ch in 'a'..'z' || ch in 'A'..'Z' || ch in '0'..'9' || ch == '_' || ch == '-' || ch == '+' || ch == '.'

    private fun skipWhitespace() {
        while (ptr < src.length && src[ptr].isWhitespace()) ptr++
    }

    private fun peek(): Char = src.getOrNull(ptr) ?: '\u0000'

    private fun expect(expected: Char) {
        require(peek() == expected) { "Expected '$expected' at index $ptr, found '${peek()}'" }
        ptr++
    }

    public fun StringBuilder.appendCodePoint(codePoint: Int) {
        when (codePoint) {
            in 0x0000..0xFFFF -> append(codePoint.toChar())
            in 0x10000..0x10FFFF -> {
                val cpPrime = codePoint - 0x10000
                val high = ((cpPrime shr 10) + 0xD800).toChar()
                val low = ((cpPrime and 0x3FF) + 0xDC00).toChar()
                append(high)
                append(low)
            }

            else -> error("Invalid Unicode code point: 0x${codePoint.toString(16)}")
        }
    }
}

public fun snbt(src: String): NBTTag = SNBTDecoder(src).parse()