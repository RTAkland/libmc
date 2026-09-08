/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/8
 */

package cn.rtast.libmc.snbt

@DslMarker
public annotation class SnbtDsl

@SnbtDsl
public abstract class BaseSnbtBuilder(
    protected val sb: StringBuilder,
    protected val prettyPrint: Boolean,
    protected val indentLevel: Int,
) {
    public var isFirst: Boolean = true

    protected fun appendSeparator() {
        if (!isFirst) {
            sb.append(if (prettyPrint) ",\n" else ",")
        } else {
            if (prettyPrint) sb.append('\n')
            isFirst = false
        }
        if (prettyPrint) {
            sb.append("  ".repeat(indentLevel + 1))
        }
    }

    protected inline fun buildBlock(openChar: Char, closeChar: Char, action: () -> Unit) {
        sb.append(openChar)
        action()
        if (prettyPrint && !isFirst) {
            sb.append('\n').append("  ".repeat(indentLevel))
        }
        sb.append(closeChar)
    }
}

@SnbtDsl
public class SnbtCompoundBuilder(
    sb: StringBuilder = StringBuilder(),
    prettyPrint: Boolean = false,
    indentLevel: Int = 0,
) : BaseSnbtBuilder(sb, prettyPrint, indentLevel) {

    private fun appendKey(key: String) {
        appendSeparator()
        sb.append(escapeSNBTKey(key)).append(if (prettyPrint) ": " else ":")
    }

    public infix fun String.byte(value: Byte) {
        appendKey(this); sb.append(value).append('b')
    }

    public infix fun String.boolean(value: Boolean) {
        appendKey(this); sb.append(if (value) "1b" else "0b")
    }

    public infix fun String.short(value: Short) {
        appendKey(this); sb.append(value).append('s')
    }

    public infix fun String.int(value: Int) {
        appendKey(this); sb.append(value)
    }

    public infix fun String.long(value: Long) {
        appendKey(this); sb.append(value).append('L')
    }

    public infix fun String.float(value: Float) {
        appendKey(this); sb.append(value).append('f')
    }

    public infix fun String.double(value: Double) {
        appendKey(this); sb.append(value).append('d')
    }

    public infix fun String.string(value: String) {
        appendKey(this); sb.append(escapeSNBTString(value))
    }

    public infix fun String.byteArray(value: ByteArray) {
        appendKey(this)
        sb.append(value.joinToString(prefix = "[B; ", postfix = "]", separator = ", ") { "${it}b" })
    }

    public infix fun String.intArray(value: IntArray) {
        appendKey(this)
        sb.append(value.joinToString(prefix = "[I; ", postfix = "]", separator = ", ") { "$it" })
    }

    public infix fun String.longArray(value: LongArray) {
        appendKey(this)
        sb.append(value.joinToString(prefix = "[L; ", postfix = "]", separator = ", ") { "${it}L" })
    }

    public infix fun String.compound(block: SnbtCompoundBuilder.() -> Unit) {
        appendKey(this)
        SnbtCompoundBuilder(sb, prettyPrint, indentLevel + 1).buildInternal(block)
    }

    public infix fun String.list(block: SnbtListBuilder.() -> Unit) {
        appendKey(this)
        SnbtListBuilder(sb, prettyPrint, indentLevel + 1).buildInternal(block)
    }

    internal fun buildInternal(block: SnbtCompoundBuilder.() -> Unit): String {
        buildBlock('{', '}') { this.block() }
        return sb.toString()
    }
}

@SnbtDsl
public class SnbtListBuilder(
    sb: StringBuilder,
    prettyPrint: Boolean,
    indentLevel: Int,
) : BaseSnbtBuilder(sb, prettyPrint, indentLevel) {
    public fun byte(value: Byte) {
        appendSeparator()
        sb.append(value).append('b')
    }

    public fun short(value: Short) {
        appendSeparator()
        sb.append(value).append('s')
    }

    public fun int(value: Int) {
        appendSeparator()
        sb.append(value)
    }

    public fun long(value: Long) {
        appendSeparator()
        sb.append(value).append('L')
    }

    public fun float(value: Float) {
        appendSeparator()
        sb.append(value).append('f')
    }

    public fun double(value: Double) {
        appendSeparator()
        sb.append(value).append('d')
    }

    public fun string(value: String) {
        appendSeparator()
        sb.append(escapeSNBTString(value))
    }

    public fun compound(block: SnbtCompoundBuilder.() -> Unit) {
        appendSeparator()
        SnbtCompoundBuilder(sb, prettyPrint, indentLevel + 1).buildInternal(block)
    }

    internal fun buildInternal(block: SnbtListBuilder.() -> Unit) = buildBlock('[', ']') { this.block() }
}

public fun buildSNBT(
    prettyPrint: Boolean = false,
    block: SnbtCompoundBuilder.() -> Unit,
): String {
    return SnbtCompoundBuilder(prettyPrint = prettyPrint).buildInternal(block)
}
