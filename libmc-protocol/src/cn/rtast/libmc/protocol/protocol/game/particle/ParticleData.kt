/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/9
 */


package cn.rtast.libmc.protocol.protocol.game.particle

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.readBlockPos
import cn.rtast.libmc.protocol.protocol.game.color.Color24
import cn.rtast.libmc.protocol.protocol.game.color.readColor24
import cn.rtast.libmc.protocol.protocol.game.item.slot.Slot
import cn.rtast.libmc.protocol.protocol.game.item.slot.readSlot
import cn.rtast.libmc.protocol.protocol.game.math.Vec3d
import cn.rtast.libmc.protocol.protocol.game.math.readVec3d

public sealed interface ParticleData {
    public object Empty : ParticleData
    public data class Block(val blockStateId: Int) : ParticleData
    public data class Geyser(val waterBlocks: Int) : ParticleData
    public data class GeyserBase(val waterBlocks: Int, val burstImpulseBase: Float) : ParticleData
    public data class GeyserPoof(val waterBlocks: Int, val burstImpulseBase: Float) : ParticleData
    public data class GeyserPlume(val waterBlocks: Int) : ParticleData
    public data class DragonBreath(val power: Float) : ParticleData
    public data class Dust(val color: Color24, val scale: Float) : ParticleData
    public data class DustColorTransition(val fromColor: Color24, val toColor: Color24, val scale: Float) :
        ParticleData

    public data class Effect(val color: Color24, val power: Float) : ParticleData
    public data class ColorARGB(val colorARGB: Int) : ParticleData
    public data class SculkCharge(val roll: Float) : ParticleData
    public data class Vibration(val source: VibrationSource, val ticks: Int) : ParticleData {
        public sealed interface VibrationSource {
            public data class Block(val position: BlockPos) : VibrationSource
            public data class Entity(val entityId: Int, val eyeHeight: Float) : VibrationSource
        }
    }

    public data class Trail(val position: Vec3d, val color: Color24, val durationTicks: Int) : ParticleData
    public data class Shriek(val delay: Int) : ParticleData
    public data class Item(val slot: Slot) : ParticleData
}

internal fun BytesBuffer.readParticleData(id: Int): ParticleData = readParticleData(ParticleType.fromID(id))

internal fun BytesBuffer.readParticleData(type: ParticleType): ParticleData {
    return when (type) {
        ParticleType.ANGRY_VILLAGER,
        ParticleType.BUBBLE,
        ParticleType.SULFUR_BUBBLES,
        ParticleType.NOXIOUS_GAS,
        ParticleType.NOXIOUS_GAS_CLOUD,
        ParticleType.CLOUD,
        ParticleType.COPPER_FIRE_FLAME,
        ParticleType.CRIT,
        ParticleType.DAMAGE_INDICATOR,
        ParticleType.DRIPPING_LAVA,
        ParticleType.FALLING_LAVA,
        ParticleType.LANDING_LAVA,
        ParticleType.DRIPPING_WATER,
        ParticleType.FALLING_WATER,
        ParticleType.ELDER_GUARDIAN,
        ParticleType.ENCHANTED_HIT,
        ParticleType.ENCHANT,
        ParticleType.END_ROD,
        ParticleType.EXPLOSION_EMITTER,
        ParticleType.EXPLOSION,
        ParticleType.GUST,
        ParticleType.SMALL_GUST,
        ParticleType.GUST_EMITTER_LARGE,
        ParticleType.GUST_EMITTER_SMALL,
        ParticleType.SONIC_BOOM,
        ParticleType.FIREWORK,
        ParticleType.FISHING,
        ParticleType.FLAME,
        ParticleType.INFESTED,
        ParticleType.CHERRY_LEAVES,
        ParticleType.PALE_OAK_LEAVES,
        ParticleType.SCULK_SOUL,
        ParticleType.SCULK_CHARGE_POP,
        ParticleType.SOUL_FIRE_FLAME,
        ParticleType.SOUL,
        ParticleType.HAPPY_VILLAGER,
        ParticleType.COMPOSTER,
        ParticleType.HEART,
        ParticleType.PAUSE_MOB_GROWTH,
        ParticleType.RESET_MOB_GROWTH,
        ParticleType.ITEM_SLIME,
        ParticleType.ITEM_COBWEB,
        ParticleType.ITEM_SNOWBALL,
        ParticleType.LARGE_SMOKE,
        ParticleType.LAVA,
        ParticleType.MYCELIUM,
        ParticleType.NOTE,
        ParticleType.POOF,
        ParticleType.PORTAL,
        ParticleType.RAIN,
        ParticleType.SMOKE,
        ParticleType.WHITE_SMOKE,
        ParticleType.SNEEZE,
        ParticleType.SPIT,
        ParticleType.SQUID_INK,
        ParticleType.SWEEP_ATTACK,
        ParticleType.TOTEM_OF_UNDYING,
        ParticleType.UNDERWATER,
        ParticleType.SPLASH,
        ParticleType.WITCH,
        ParticleType.BUBBLE_POP,
        ParticleType.CURRENT_DOWN,
        ParticleType.BUBBLE_COLUMN_UP,
        ParticleType.NAUTILUS,
        ParticleType.DOLPHIN,
        ParticleType.CAMPFIRE_COSY_SMOKE,
        ParticleType.CAMPFIRE_SIGNAL_SMOKE,
        ParticleType.DRIPPING_HONEY,
        ParticleType.FALLING_HONEY,
        ParticleType.LANDING_HONEY,
        ParticleType.FALLING_NECTAR,
        ParticleType.FALLING_SPORE_BLOSSOM,
        ParticleType.ASH,
        ParticleType.CRIMSON_SPORE,
        ParticleType.WARPED_SPORE,
        ParticleType.SPORE_BLOSSOM_AIR,
        ParticleType.DRIPPING_OBSIDIAN_TEAR,
        ParticleType.FALLING_OBSIDIAN_TEAR,
        ParticleType.LANDING_OBSIDIAN_TEAR,
        ParticleType.REVERSE_PORTAL,
        ParticleType.WHITE_ASH,
        ParticleType.SMALL_FLAME,
        ParticleType.SNOWFLAKE,
        ParticleType.DRIPPING_DRIPSTONE_LAVA,
        ParticleType.FALLING_DRIPSTONE_LAVA,
        ParticleType.DRIPPING_DRIPSTONE_WATER,
        ParticleType.FALLING_DRIPSTONE_WATER,
        ParticleType.GLOW_SQUID_INK,
        ParticleType.GLOW,
        ParticleType.WAX_ON,
        ParticleType.WAX_OFF,
        ParticleType.ELECTRIC_SPARK,
        ParticleType.SCRAPE,
        ParticleType.EGG_CRACK,
        ParticleType.DUST_PLUME,
        ParticleType.VAULT_CONNECTION,
        ParticleType.OMINOUS_SPAWNING,
        ParticleType.RAID_OMEN,
        ParticleType.TRIAL_OMEN,
        ParticleType.FIREFLY,
        ParticleType.SULFUR_CUBE_GOO,
        ParticleType.TRIAL_SPAWNER_DETECTION,
        ParticleType.TRIAL_SPAWNER_DETECTION_OMINOUS,
            -> ParticleData.Empty

        ParticleType.BLOCK,
        ParticleType.BLOCK_MARKER,
        ParticleType.FALLING_DUST,
        ParticleType.DUST_PILLAR,
        ParticleType.BLOCK_CRUMBLE,
            -> ParticleData.Block(this.readVarInt())

        ParticleType.GEYSER -> ParticleData.Geyser(this.readInt())
        ParticleType.GEYSER_BASE -> ParticleData.GeyserBase(this.readInt(), this.readFloat())
        ParticleType.GEYSER_POOF -> ParticleData.GeyserPoof(this.readInt(), this.readFloat())
        ParticleType.GEYSER_PLUME -> ParticleData.GeyserPlume(this.readInt())
        ParticleType.DRAGON_BREATH -> ParticleData.DragonBreath(this.readFloat())
        ParticleType.SCULK_CHARGE -> ParticleData.SculkCharge(this.readFloat())
        ParticleType.SHRIEK -> ParticleData.Shriek(this.readVarInt())
        ParticleType.ENTITY_EFFECT,
        ParticleType.TINTED_LEAVES,
        ParticleType.FLASH,
            -> ParticleData.ColorARGB(this.readInt())

        ParticleType.DUST -> ParticleData.Dust(this.readColor24(), this.readFloat())
        ParticleType.DUST_COLOR_TRANSITION -> ParticleData.DustColorTransition(
            this.readColor24(),
            this.readColor24(),
            this.readFloat()
        )

        ParticleType.EFFECT,
        ParticleType.INSTANT_EFFECT,
            -> ParticleData.Effect(this.readColor24(), this.readFloat())

        ParticleType.ITEM -> ParticleData.Item(readSlot())
        ParticleType.VIBRATION -> {
            val source = when (val sourceTypeId = this.readVarInt()) {
                0 -> ParticleData.Vibration.VibrationSource.Block(this.readBlockPos())
                1 -> ParticleData.Vibration.VibrationSource.Entity(this.readVarInt(), this.readFloat())
                else -> error("Unknown vibration source type: $sourceTypeId")
            }
            ParticleData.Vibration(source, this.readVarInt())
        }

        ParticleType.TRAIL -> ParticleData.Trail(readVec3d(), readColor24(), this.readVarInt())
    }
}