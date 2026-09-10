/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.potion

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.IdSet
import cn.rtast.libmc.primitives.readIdSet
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.primitives.writeIdSet
import cn.rtast.libmc.primitives.writePrefixed
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.sound.SoundEvent
import cn.rtast.libmc.protocol.protocol.game.sound.readSoundEvent
import cn.rtast.libmc.protocol.protocol.game.sound.writeSoundEvent

public sealed interface ConsumeEffect {
    public data class ApplyEffects(val effects: List<PotionEffect>, val probability: Float) : ConsumeEffect
    public data class RemoveEffects(val effects: IdSet) : ConsumeEffect
    public data object ClearAllEffects : ConsumeEffect
    public data class TeleportRandomly(val diameter: Float) : ConsumeEffect
    public data class PlaySound(val sound: SoundEvent) : ConsumeEffect
}


internal fun BytesBuffer.readConsumeEffects(): List<ConsumeEffect> {
    return readPrefixed {
        when (val typeId = readVarInt()) {
            0 -> {
                val effects = readPrefixed { readPotionEffect() }
                val probability = readFloat()
                ConsumeEffect.ApplyEffects(effects, probability)
            }

            1 -> {
                val effects = readIdSet()
                ConsumeEffect.RemoveEffects(effects)
            }

            2 -> ConsumeEffect.ClearAllEffects
            3 -> {
                val diameter = readFloat()
                ConsumeEffect.TeleportRandomly(diameter)
            }

            4 -> {
                val sound = readSoundEvent()
                ConsumeEffect.PlaySound(sound)
            }

            else -> throw IllegalArgumentException("Unknown ConsumeEffect type ID: $typeId")
        }
    }
}

internal fun BytesBuffer.writeConsumeEffects(effects: List<ConsumeEffect>) {
    writePrefixed(effects) { effect ->
        when (effect) {
            is ConsumeEffect.ApplyEffects -> {
                writeVarInt(0)
                writePrefixed(effect.effects) { writePotionEffect(it) }
                writeFloat(effect.probability)
            }

            is ConsumeEffect.RemoveEffects -> {
                writeVarInt(1)
                writeIdSet(effect.effects)
            }

            is ConsumeEffect.ClearAllEffects -> {
                writeVarInt(2)
            }

            is ConsumeEffect.TeleportRandomly -> {
                writeVarInt(3)
                writeFloat(effect.diameter)
            }

            is ConsumeEffect.PlaySound -> {
                writeVarInt(4)
                writeSoundEvent(effect.sound)
            }
        }
    }
}