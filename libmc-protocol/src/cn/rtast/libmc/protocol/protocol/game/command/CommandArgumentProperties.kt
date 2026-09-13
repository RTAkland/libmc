/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.protocol.protocol.game.command

import cn.rtast.libmc.protocol.protocol.game.Identifier

public sealed interface CommandArgumentProperties {
    public object Empty : CommandArgumentProperties

    public data class IntProp(val min: Int = Int.MIN_VALUE, val max: Int = Int.MAX_VALUE) :
        CommandArgumentProperties

    public data class LongProp(val min: Long = Long.MIN_VALUE, val max: Long = Long.MAX_VALUE) :
        CommandArgumentProperties

    public data class FloatProp(val min: Float = Float.MIN_VALUE, val max: Float = Float.MAX_VALUE) :
        CommandArgumentProperties

    public data class DoubleProp(val min: Double = Double.MIN_VALUE, val max: Double = Double.MAX_VALUE) :
        CommandArgumentProperties

    public data class StringProp(val behavior: StringBehavior) : CommandArgumentProperties {
        public enum class StringBehavior(public val id: Int) {
            SingleWord(0), QuotablePhrase(1), GreedyPhrase(2);

            public companion object {
                public fun fromId(id: Int): StringBehavior = entries.first { it.id == id }
            }
        }
    }

    public data class Entity(val singleOnly: Boolean, val playersOnly: Boolean) : CommandArgumentProperties

    public data class ScoreHolder(val allowMultiple: Boolean) : CommandArgumentProperties
    public data class Time(val min: Int) : CommandArgumentProperties
    public data class Registry(val registry: Identifier) : CommandArgumentProperties
}