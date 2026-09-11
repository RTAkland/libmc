/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.data.component

import cn.rtast.libmc.nbt.NBTCompound
import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.*
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.block.BlockPos
import cn.rtast.libmc.protocol.protocol.game.block.BlockStateProperty
import cn.rtast.libmc.protocol.protocol.game.block.readBlockPos
import cn.rtast.libmc.protocol.protocol.game.block.writeBlockPos
import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.game.chat.readTextComponent
import cn.rtast.libmc.protocol.protocol.game.chat.writeTextComponent
import cn.rtast.libmc.protocol.protocol.game.color.DyeColor
import cn.rtast.libmc.protocol.protocol.game.data.component.attributes.*
import cn.rtast.libmc.protocol.protocol.game.entity.*
import cn.rtast.libmc.protocol.protocol.game.item.ItemUseAnimation
import cn.rtast.libmc.protocol.protocol.game.item.PaintingVariantType
import cn.rtast.libmc.protocol.protocol.game.item.readPaintingVariantType
import cn.rtast.libmc.protocol.protocol.game.item.slot.ItemStack
import cn.rtast.libmc.protocol.protocol.game.item.slot.readItemStack
import cn.rtast.libmc.protocol.protocol.game.item.slot.writeItemStack
import cn.rtast.libmc.protocol.protocol.game.item.writePaintingVariantType
import cn.rtast.libmc.protocol.protocol.game.potion.*
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.session.ResolvableProfile
import cn.rtast.libmc.protocol.protocol.game.session.readResolvableProfile
import cn.rtast.libmc.protocol.protocol.game.session.writeResolvableProfile
import cn.rtast.libmc.protocol.protocol.game.sound.SoundEvent
import cn.rtast.libmc.protocol.protocol.game.sound.readSoundEvent
import cn.rtast.libmc.protocol.protocol.game.sound.writeSoundEvent
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier
import cn.rtast.libmc.protocol.protocol.util.readNetworkNBTCompound
import cn.rtast.libmc.protocol.protocol.util.writeNetworkNBTCompound

public sealed interface DataComponent {
    public data class CustomDataComponent(val nbt: NBTCompound) : DataComponent {
        internal companion object Codec : PacketCodec<CustomDataComponent> {
            override fun encode(buffer: BytesBuffer, value: CustomDataComponent) {
                buffer.writeNetworkNBTCompound(value.nbt)
            }

            override fun decode(buffer: BytesBuffer): CustomDataComponent {
                return CustomDataComponent(buffer.readNetworkNBTCompound())
            }
        }
    }

    public data class MaxStackSizeComponent(val size: Int) : DataComponent {
        internal companion object Codec : PacketCodec<MaxStackSizeComponent> {
            override fun encode(buffer: BytesBuffer, value: MaxStackSizeComponent) {
                require(value.size in 1..99)
                buffer.writeVarInt(value.size)
            }

            override fun decode(buffer: BytesBuffer): MaxStackSizeComponent =
                MaxStackSizeComponent(buffer.readVarInt())
        }
    }

    public data class MaxDamageComponent(val maxDamage: Int) : DataComponent {
        internal companion object Codec : PacketCodec<MaxDamageComponent> {
            override fun encode(buffer: BytesBuffer, value: MaxDamageComponent) {
                buffer.writeVarInt(value.maxDamage)
            }

            override fun decode(buffer: BytesBuffer): MaxDamageComponent =
                MaxDamageComponent(buffer.readVarInt())
        }
    }

    public data class DamageComponent(val damage: Int) : DataComponent {
        internal companion object Codec : PacketCodec<DamageComponent> {
            override fun encode(buffer: BytesBuffer, value: DamageComponent) {
                buffer.writeVarInt(value.damage)
            }

            override fun decode(buffer: BytesBuffer): DamageComponent =
                DamageComponent(buffer.readVarInt())
        }
    }

    public data object UnbreakableComponent : DataComponent, PacketCodec<UnbreakableComponent> {
        override fun encode(buffer: BytesBuffer, value: UnbreakableComponent) {}
        override fun decode(buffer: BytesBuffer): UnbreakableComponent = UnbreakableComponent
    }

    public data class CustomNameComponent(val name: TextComponent) : DataComponent {
        internal companion object Codec : PacketCodec<CustomNameComponent> {
            override fun encode(buffer: BytesBuffer, value: CustomNameComponent) {
                buffer.writeTextComponent(value.name)
            }

            override fun decode(buffer: BytesBuffer): CustomNameComponent =
                CustomNameComponent(buffer.readTextComponent())
        }
    }

    public data class ItemNameComponent(val name: TextComponent) : DataComponent {
        internal companion object Codec : PacketCodec<ItemNameComponent> {
            override fun encode(buffer: BytesBuffer, value: ItemNameComponent) {
                buffer.writeTextComponent(value.name)
            }

            override fun decode(buffer: BytesBuffer): ItemNameComponent =
                ItemNameComponent(buffer.readTextComponent())
        }
    }

    public data class ItemModelComponent(val model: Identifier) : DataComponent {
        internal companion object Codec : PacketCodec<ItemModelComponent> {
            override fun encode(buffer: BytesBuffer, value: ItemModelComponent) {
                buffer.writeIdentifier(value.model)
            }

            override fun decode(buffer: BytesBuffer): ItemModelComponent =
                ItemModelComponent(buffer.readIdentifier())
        }
    }

    public data class LoreComponent(val lines: List<TextComponent>) : DataComponent {
        internal companion object Codec : PacketCodec<LoreComponent> {
            override fun encode(buffer: BytesBuffer, value: LoreComponent) {
                buffer.writePrefixed(value.lines) { writeTextComponent(it) }
            }

            override fun decode(buffer: BytesBuffer): LoreComponent =
                LoreComponent(buffer.readPrefixed { readTextComponent() })
        }
    }

    public data class RarityComponent(val rarity: ItemRarity) : DataComponent {
        internal companion object Codec : PacketCodec<RarityComponent> {
            override fun encode(buffer: BytesBuffer, value: RarityComponent) {
                buffer.writeVarInt(value.rarity.id)
            }

            override fun decode(buffer: BytesBuffer): RarityComponent =
                RarityComponent(ItemRarity.fromID(buffer.readVarInt()))
        }
    }

    public data class EnchantmentsComponent(val enchantments: List<EnchantmentEntry>) : DataComponent {
        internal companion object Codec : PacketCodec<EnchantmentsComponent> {
            override fun encode(buffer: BytesBuffer, value: EnchantmentsComponent) {
                buffer.writePrefixed(value.enchantments) {
                    writeVarInt(it.typeId)
                    writeVarInt(it.level)
                }
            }

            override fun decode(buffer: BytesBuffer): EnchantmentsComponent =
                EnchantmentsComponent(buffer.readPrefixed {
                    val typeId = readVarInt()
                    val level = readVarInt()
                    EnchantmentEntry(typeId, level)
                })
        }
    }

    public data class CanPlaceOnComponent(val predicates: List<BlockPredicate>) : DataComponent {
        internal companion object Codec : PacketCodec<CanPlaceOnComponent> {
            override fun encode(buffer: BytesBuffer, value: CanPlaceOnComponent) {
                buffer.writePrefixed(value.predicates) { writeBlockPredicate(it) }
            }

            override fun decode(buffer: BytesBuffer): CanPlaceOnComponent =
                CanPlaceOnComponent(buffer.readPrefixed { readBlockPredicate() })
        }
    }

    public data class CanBreakComponent(val predicates: List<BlockPredicate>) : DataComponent {
        internal companion object Codec : PacketCodec<CanBreakComponent> {
            override fun encode(buffer: BytesBuffer, value: CanBreakComponent) {
                buffer.writePrefixed(value.predicates) { writeBlockPredicate(it) }
            }

            override fun decode(buffer: BytesBuffer): CanBreakComponent =
                CanBreakComponent(buffer.readPrefixed { readBlockPredicate() })
        }
    }

    public data class AttributeModifiersComponent(val modifiers: List<AttributeModifierEntry>) :
        DataComponent {
        internal companion object Codec : PacketCodec<AttributeModifiersComponent> {
            override fun encode(buffer: BytesBuffer, value: AttributeModifiersComponent) {
                buffer.writePrefixed(value.modifiers) { entry ->
                    writeVarInt(entry.attributeId)
                    writeIdentifier(entry.modifierId)
                    writeDouble(entry.value)
                    writeVarInt(entry.operation.id)
                    writeVarInt(entry.slot.id)
                }
            }

            override fun decode(buffer: BytesBuffer): AttributeModifiersComponent =
                AttributeModifiersComponent(buffer.readPrefixed {
                    val attributeId = readVarInt()
                    val modifierId = readIdentifier()
                    val value = readDouble()
                    val operation = AttributeOperation.fromID(readVarInt())
                    val slot = AttributeSlot.fromID(readVarInt())
                    AttributeModifierEntry(attributeId, modifierId, value, operation, slot)
                })
        }
    }

    public data class CustomModelDataComponent(
        val floats: List<Float>,
        val flags: List<Boolean>,
        val strings: List<String>,
        val colors: List<Int>,
    ) : DataComponent {
        internal companion object Codec : PacketCodec<CustomModelDataComponent> {
            override fun encode(buffer: BytesBuffer, value: CustomModelDataComponent) {
                buffer.writePrefixed(value.floats) { writeFloat(it) }
                buffer.writePrefixed(value.flags) { writeBoolean(it) }
                buffer.writePrefixed(value.strings) { writeMcString(it) }
                buffer.writePrefixed(value.colors) { writeInt(it) }
            }

            override fun decode(buffer: BytesBuffer): CustomModelDataComponent {
                val floats = buffer.readPrefixed { readFloat() }
                val flags = buffer.readPrefixed { readBoolean() }
                val strings = buffer.readPrefixed { readMcString() }
                val colors = buffer.readPrefixed { readInt() }
                return CustomModelDataComponent(floats, flags, strings, colors)
            }
        }
    }

    public data class TooltipDisplayComponent(val hideTooltip: Boolean, val hiddenComponents: List<Int>) :
        DataComponent {
        internal companion object Codec : PacketCodec<TooltipDisplayComponent> {
            override fun encode(buffer: BytesBuffer, value: TooltipDisplayComponent) {
                buffer.writeBoolean(value.hideTooltip)
                buffer.writePrefixed(value.hiddenComponents) { writeVarInt(it) }
            }

            override fun decode(buffer: BytesBuffer): TooltipDisplayComponent {
                val hideTooltip = buffer.readBoolean()
                val hiddenComponents = buffer.readPrefixed { readVarInt() }
                return TooltipDisplayComponent(hideTooltip, hiddenComponents)
            }
        }
    }

    public data class RepairCostComponent(val cost: Int) : DataComponent {
        internal companion object Codec : PacketCodec<RepairCostComponent> {
            override fun encode(buffer: BytesBuffer, value: RepairCostComponent) {
                buffer.writeVarInt(value.cost)
            }

            override fun decode(buffer: BytesBuffer): RepairCostComponent = RepairCostComponent(buffer.readVarInt())
        }
    }

    public data object CreativeSlotLockComponent : DataComponent, PacketCodec<CreativeSlotLockComponent> {
        override fun encode(buffer: BytesBuffer, value: CreativeSlotLockComponent) {}
        override fun decode(buffer: BytesBuffer): CreativeSlotLockComponent = CreativeSlotLockComponent
    }

    public data class EnchantmentGlintOverrideComponent(val hasGlint: Boolean) : DataComponent {
        internal companion object Codec : PacketCodec<EnchantmentGlintOverrideComponent> {
            override fun encode(buffer: BytesBuffer, value: EnchantmentGlintOverrideComponent) {
                buffer.writeBoolean(value.hasGlint)
            }

            override fun decode(buffer: BytesBuffer): EnchantmentGlintOverrideComponent =
                EnchantmentGlintOverrideComponent(buffer.readBoolean())
        }
    }

    public data class IntangibleProjectileComponent(val empty: NBTCompound) : DataComponent {
        internal companion object Codec : PacketCodec<IntangibleProjectileComponent> {
            override fun encode(buffer: BytesBuffer, value: IntangibleProjectileComponent) {
                buffer.writeNetworkNBTCompound(value.empty)
            }

            override fun decode(buffer: BytesBuffer): IntangibleProjectileComponent =
                IntangibleProjectileComponent(buffer.readNetworkNBTCompound())
        }
    }

    public data class FoodComponent(val nutrition: Int, val saturationModifier: Float, val canAlwaysEat: Boolean) :
        DataComponent {
        internal companion object Codec : PacketCodec<FoodComponent> {
            override fun encode(buffer: BytesBuffer, value: FoodComponent) {
                buffer.writeVarInt(value.nutrition)
                buffer.writeFloat(value.saturationModifier)
                buffer.writeBoolean(value.canAlwaysEat)
            }

            override fun decode(buffer: BytesBuffer): FoodComponent {
                val nutrition = buffer.readVarInt()
                val saturationModifier = buffer.readFloat()
                val canAlwaysEat = buffer.readBoolean()
                return FoodComponent(nutrition, saturationModifier, canAlwaysEat)
            }
        }
    }

    public data class ConsumableComponent(
        val consumeSeconds: Float,
        val animation: ItemUseAnimation,
        val sound: IdOrX<SoundEvent>,
        val hasConsumeParticles: Boolean,
        val effects: List<ConsumeEffect>,
    ) : DataComponent {
        internal companion object Codec : PacketCodec<ConsumableComponent> {
            override fun encode(buffer: BytesBuffer, value: ConsumableComponent) {
                buffer.writeFloat(value.consumeSeconds)
                buffer.writeVarInt(value.animation.id)
                buffer.writeIdOrX(value.sound) { writeSoundEvent(it) }
                buffer.writeBoolean(value.hasConsumeParticles)
                buffer.writeConsumeEffects(value.effects)
            }

            override fun decode(buffer: BytesBuffer): ConsumableComponent {
                val consumeSeconds = buffer.readFloat()
                val animation = ItemUseAnimation.fromID(buffer.readVarInt())
                val sound = buffer.readIdOrX { readSoundEvent() }
                val hasConsumeParticles = buffer.readBoolean()
                val effects = buffer.readConsumeEffects()
                return ConsumableComponent(consumeSeconds, animation, sound, hasConsumeParticles, effects)
            }
        }
    }

    public data class UseRemainderComponent(val remainder: ItemStack) : DataComponent {
        internal companion object Codec : PacketCodec<UseRemainderComponent> {
            override fun encode(buffer: BytesBuffer, value: UseRemainderComponent) {
                buffer.writeItemStack(value.remainder)
            }

            override fun decode(buffer: BytesBuffer): UseRemainderComponent =
                UseRemainderComponent(buffer.readItemStack())
        }
    }

    public data class UseCooldownComponent(val seconds: Float, val cooldownGroup: Identifier?) : DataComponent {
        internal companion object Codec : PacketCodec<UseCooldownComponent> {
            override fun encode(buffer: BytesBuffer, value: UseCooldownComponent) {
                buffer.writeFloat(value.seconds)
                buffer.writePrefixedOptional(value.cooldownGroup) { writeIdentifier(it) }
            }

            override fun decode(buffer: BytesBuffer): UseCooldownComponent {
                val seconds = buffer.readFloat()
                val cooldownGroup = buffer.readIdentifier()
                return UseCooldownComponent(seconds, cooldownGroup)
            }
        }
    }

    public data class UseEffectsComponent(
        val canSprint: Boolean,
        val interactVibrations: Boolean,
        val speedMultiplier: Float,
    ) :
        DataComponent {
        internal companion object Codec : PacketCodec<UseEffectsComponent> {
            override fun encode(buffer: BytesBuffer, value: UseEffectsComponent) {
                buffer.writeBoolean(value.canSprint)
                buffer.writeBoolean(value.interactVibrations)
                buffer.writeFloat(value.speedMultiplier)
            }

            override fun decode(buffer: BytesBuffer): UseEffectsComponent {
                val canSprint = buffer.readBoolean()
                val interactVibrations = buffer.readBoolean()
                val speedMultiplier = buffer.readFloat()
                return UseEffectsComponent(canSprint, interactVibrations, speedMultiplier)
            }
        }
    }

    public data class MinimumAttackChargeComponent(val minimumAttackChange: Float) : DataComponent {
        internal companion object Codec : PacketCodec<MinimumAttackChargeComponent> {
            override fun encode(buffer: BytesBuffer, value: MinimumAttackChargeComponent) {
                buffer.writeFloat(value.minimumAttackChange)
            }

            override fun decode(buffer: BytesBuffer): MinimumAttackChargeComponent =
                MinimumAttackChargeComponent(buffer.readFloat())
        }
    }

    public data class DamageTypeComponent(val type: Int) : DataComponent {
        internal companion object Codec : PacketCodec<DamageTypeComponent> {
            override fun encode(buffer: BytesBuffer, value: DamageTypeComponent) {
                buffer.writeVarInt(value.type)
            }

            override fun decode(buffer: BytesBuffer): DamageTypeComponent =
                DamageTypeComponent(buffer.readVarInt())
        }
    }

    public data class DamageResistantComponent(val types: IdSet) : DataComponent {
        internal companion object Codec : PacketCodec<DamageResistantComponent> {
            override fun encode(buffer: BytesBuffer, value: DamageResistantComponent) {
                buffer.writeIdSet(value.types)
            }

            override fun decode(buffer: BytesBuffer): DamageResistantComponent =
                DamageResistantComponent(buffer.readIdSet())
        }
    }

    public data class AttackRangeComponent(
        val minReach: Float,
        val maxReach: Float,
        val minCreativeReach: Float,
        val maxCreativeReach: Float,
        val hitBoxMargin: Float,
        val mobFactor: Float,
    ) : DataComponent {
        internal companion object Codec : PacketCodec<AttackRangeComponent> {
            override fun encode(buffer: BytesBuffer, value: AttackRangeComponent) {
                buffer.writeFloat(value.minReach)
                buffer.writeFloat(value.maxReach)
                buffer.writeFloat(value.minCreativeReach)
                buffer.writeFloat(value.maxCreativeReach)
                buffer.writeFloat(value.hitBoxMargin)
                buffer.writeFloat(value.mobFactor)
            }

            override fun decode(buffer: BytesBuffer): AttackRangeComponent {
                val minReach = buffer.readFloat()
                val maxReach = buffer.readFloat()
                val minCreativeReach = buffer.readFloat()
                val maxCreativeReach = buffer.readFloat()
                val hitBoxMargin = buffer.readFloat()
                val mobFactor = buffer.readFloat()
                return AttackRangeComponent(
                    minReach,
                    maxReach,
                    minCreativeReach,
                    maxCreativeReach,
                    hitBoxMargin,
                    mobFactor
                )
            }
        }
    }

    public data class ToolComponent(
        val rules: List<ToolRule>,
        val defaultMiningSpeed: Float,
        val damagePerBlock: Int,
        val canDestroyBlocksInCreative: Boolean,
    ) : DataComponent {
        internal companion object Codec : PacketCodec<ToolComponent> {
            override fun encode(buffer: BytesBuffer, value: ToolComponent) {
                buffer.writePrefixed(value.rules) { rule ->
                    writeIdSet(rule.blocks)
                    writePrefixedOptional(rule.speed) { writeFloat(it) }
                    writePrefixedOptional(rule.correctForDrops) { writeBoolean(it) }
                }
                buffer.writeFloat(value.defaultMiningSpeed)
                buffer.writeVarInt(value.damagePerBlock)
                buffer.writeBoolean(value.canDestroyBlocksInCreative)
            }

            override fun decode(buffer: BytesBuffer): ToolComponent {
                val rules = buffer.readPrefixed {
                    val blocks = readIdSet()
                    val speed = readPrefixOptional { readFloat() }
                    val correctForDrops = readPrefixOptional { readBoolean() }
                    ToolRule(blocks, speed, correctForDrops)
                }
                val defaultMiningSpeed = buffer.readFloat()
                val damagePerBlock = buffer.readVarInt()
                val canDestroyBlocksInCreative = buffer.readBoolean()
                return ToolComponent(rules, defaultMiningSpeed, damagePerBlock, canDestroyBlocksInCreative)
            }
        }
    }

    public data class WeaponComponent(val damagePerAttack: Int, val disableBlockingFor: Float) : DataComponent {
        internal companion object Codec : PacketCodec<WeaponComponent> {
            override fun encode(buffer: BytesBuffer, value: WeaponComponent) {
                buffer.writeVarInt(value.damagePerAttack)
                buffer.writeFloat(value.disableBlockingFor)
            }

            override fun decode(buffer: BytesBuffer): WeaponComponent {
                val damagePerAttack = buffer.readVarInt()
                val disableBlockingFor = buffer.readFloat()
                return WeaponComponent(damagePerAttack, disableBlockingFor)
            }
        }
    }

    public data class EnchantableComponent(val value: Int) : DataComponent {
        internal companion object Codec : PacketCodec<EnchantableComponent> {
            override fun encode(buffer: BytesBuffer, value: EnchantableComponent) {
                buffer.writeVarInt(value.value)
            }

            override fun decode(buffer: BytesBuffer): EnchantableComponent =
                EnchantableComponent(buffer.readVarInt())
        }
    }

    public data class EquippableComponent(
        val slot: EquipmentSlot,
        val equipSound: IdOrX<SoundEvent>,
        val model: Identifier?,
        val cameraOverlay: Identifier?,
        val allowedEntities: IdSet?,
        val dispensable: Boolean,
        val swappable: Boolean,
        val damageOnHurt: Boolean,
        val canBeSheared: Boolean,
        val shearingSound: IdOrX<SoundEvent>,
    ) : DataComponent {
        internal companion object Codec : PacketCodec<EquippableComponent> {
            override fun encode(buffer: BytesBuffer, value: EquippableComponent) {
                buffer.writeVarInt(value.slot.id)
                buffer.writeIdOrX(value.equipSound) { writeSoundEvent(it) }
                buffer.writePrefixedOptional(value.model) { writeIdentifier(it) }
                buffer.writePrefixedOptional(value.cameraOverlay) { writeIdentifier(it) }
                buffer.writePrefixedOptional(value.allowedEntities) { writeIdSet(it) }
                buffer.writeBoolean(value.dispensable)
                buffer.writeBoolean(value.swappable)
                buffer.writeBoolean(value.damageOnHurt)
                buffer.writeBoolean(value.canBeSheared)
                buffer.writeIdOrX(value.shearingSound) { writeSoundEvent(it) }
            }

            override fun decode(buffer: BytesBuffer): EquippableComponent {
                val slot = EquipmentSlot.fromID(buffer.readVarInt())
                val equipSound = buffer.readIdOrX { readSoundEvent() }
                val model = buffer.readPrefixOptional { readIdentifier() }
                val cameraOverlay = buffer.readPrefixOptional { readIdentifier() }
                val allowedEntities = buffer.readPrefixOptional { readIdSet() }
                val dispensable = buffer.readBoolean()
                val swappable = buffer.readBoolean()
                val damageOnHurt = buffer.readBoolean()
                val canBeSheared = buffer.readBoolean()
                val shearingSound = buffer.readIdOrX { readSoundEvent() }
                return EquippableComponent(
                    slot, equipSound, model, cameraOverlay, allowedEntities,
                    dispensable, swappable, damageOnHurt, canBeSheared, shearingSound
                )
            }
        }
    }

    public data class RepairableComponent(val items: IdSet) : DataComponent {
        internal companion object Codec : PacketCodec<RepairableComponent> {
            override fun encode(buffer: BytesBuffer, value: RepairableComponent) {
                buffer.writeIdSet(value.items)
            }

            override fun decode(buffer: BytesBuffer): RepairableComponent =
                RepairableComponent(buffer.readIdSet())
        }
    }

    public data object GliderComponent : DataComponent, PacketCodec<GliderComponent> {
        override fun encode(buffer: BytesBuffer, value: GliderComponent) {}
        override fun decode(buffer: BytesBuffer): GliderComponent = GliderComponent
    }

    public data class TooltipStyleComponent(val style: Identifier) : DataComponent {
        internal companion object Codec : PacketCodec<TooltipStyleComponent> {
            override fun encode(buffer: BytesBuffer, value: TooltipStyleComponent) {
                buffer.writeIdentifier(value.style)
            }

            override fun decode(buffer: BytesBuffer): TooltipStyleComponent =
                TooltipStyleComponent(buffer.readIdentifier())
        }
    }

    public data class DeathProtectionComponent(val effects: List<ConsumeEffect>) : DataComponent {
        internal companion object Codec : PacketCodec<DeathProtectionComponent> {
            override fun encode(buffer: BytesBuffer, value: DeathProtectionComponent) {
                buffer.writeConsumeEffects(value.effects)
            }

            override fun decode(buffer: BytesBuffer): DeathProtectionComponent =
                DeathProtectionComponent(buffer.readConsumeEffects())
        }
    }

    public data class BlocksAttacksComponent(
        val blockDelaySeconds: Float,
        val disableCooldownScale: Float,
        val damageReductions: List<DamageReduction>,
        val itemDamageThreshold: Float,
        val itemDamageBase: Float,
        val itemDamageFactor: Float,
        val bypassedBy: IdSet?,
        val blockSound: IdOrX<SoundEvent>?,
        val disableSound: IdOrX<SoundEvent>?,
    ) : DataComponent {
        internal companion object Codec : PacketCodec<BlocksAttacksComponent> {
            override fun encode(buffer: BytesBuffer, value: BlocksAttacksComponent) {
                buffer.writeFloat(value.blockDelaySeconds)
                buffer.writeFloat(value.disableCooldownScale)
                buffer.writePrefixed(value.damageReductions) { reduction ->
                    writeFloat(reduction.horizontalBlockingAngle)
                    writePrefixedOptional(reduction.type) { writeIdSet(it) }
                    writeFloat(reduction.base)
                    writeFloat(reduction.factor)
                }
                buffer.writeFloat(value.itemDamageThreshold)
                buffer.writeFloat(value.itemDamageBase)
                buffer.writeFloat(value.itemDamageFactor)
                buffer.writePrefixedOptional(value.bypassedBy) { writeIdSet(it) }
                buffer.writePrefixedOptional(value.blockSound) { writeIdOrX(it) { sound -> writeSoundEvent(sound) } }
                buffer.writePrefixedOptional(value.disableSound) { writeIdOrX(it) { sound -> writeSoundEvent(sound) } }
            }

            override fun decode(buffer: BytesBuffer): BlocksAttacksComponent {
                val blockDelaySeconds = buffer.readFloat()
                val disableCooldownScale = buffer.readFloat()
                val damageReductions = buffer.readPrefixed {
                    val horizontalBlockingAngle = readFloat()
                    val type = readPrefixOptional { readIdSet() }
                    val base = readFloat()
                    val factor = readFloat()
                    DamageReduction(horizontalBlockingAngle, type, base, factor)
                }
                val itemDamageThreshold = buffer.readFloat()
                val itemDamageBase = buffer.readFloat()
                val itemDamageFactor = buffer.readFloat()
                val bypassedBy = buffer.readPrefixOptional { readIdSet() }
                val blockSound = buffer.readPrefixOptional { readIdOrX { readSoundEvent() } }
                val disableSound = buffer.readPrefixOptional { readIdOrX { readSoundEvent() } }
                return BlocksAttacksComponent(
                    blockDelaySeconds, disableCooldownScale, damageReductions,
                    itemDamageThreshold, itemDamageBase, itemDamageFactor,
                    bypassedBy, blockSound, disableSound
                )
            }
        }
    }

    public data class StoredEnchantmentsComponent(val enchantments: List<StoredEnchantment>) : DataComponent {
        internal companion object Codec : PacketCodec<StoredEnchantmentsComponent> {
            override fun encode(buffer: BytesBuffer, value: StoredEnchantmentsComponent) {
                buffer.writePrefixed(value.enchantments) { enchantment ->
                    writeVarInt(enchantment.enchantmentId)
                    writeVarInt(enchantment.level)
                }
            }

            override fun decode(buffer: BytesBuffer): StoredEnchantmentsComponent =
                StoredEnchantmentsComponent(buffer.readPrefixed {
                    val enchantmentId = buffer.readVarInt()
                    val level = buffer.readVarInt()
                    StoredEnchantment(enchantmentId, level)
                })
        }
    }

    public data class DyedColorComponent(val color: Int) : DataComponent {
        internal companion object Codec : PacketCodec<DyedColorComponent> {
            override fun encode(buffer: BytesBuffer, value: DyedColorComponent) {
                buffer.writeInt(value.color)
            }

            override fun decode(buffer: BytesBuffer): DyedColorComponent =
                DyedColorComponent(buffer.readInt())
        }
    }

    public data class MapColorComponent(val color: Int) : DataComponent {
        internal companion object Codec : PacketCodec<MapColorComponent> {
            override fun encode(buffer: BytesBuffer, value: MapColorComponent) {
                buffer.writeInt(value.color)
            }

            override fun decode(buffer: BytesBuffer): MapColorComponent =
                MapColorComponent(buffer.readInt())
        }
    }

    public data class MapIdComponent(val id: Int) : DataComponent {
        internal companion object Codec : PacketCodec<MapIdComponent> {
            override fun encode(buffer: BytesBuffer, value: MapIdComponent) {
                buffer.writeVarInt(value.id)
            }

            override fun decode(buffer: BytesBuffer): MapIdComponent =
                MapIdComponent(buffer.readVarInt())
        }
    }

    public data class MapDecorationsComponent(val nbt: NBTCompound) : DataComponent {
        internal companion object Codec : PacketCodec<MapDecorationsComponent> {
            override fun encode(buffer: BytesBuffer, value: MapDecorationsComponent) {
                buffer.writeNetworkNBTCompound(value.nbt)
            }

            override fun decode(buffer: BytesBuffer): MapDecorationsComponent =
                MapDecorationsComponent(buffer.readNetworkNBTCompound())
        }
    }

    public data class MapPostProcessingComponent(val type: MapPostProcessingType) : DataComponent {
        internal companion object Codec : PacketCodec<MapPostProcessingComponent> {
            override fun encode(buffer: BytesBuffer, value: MapPostProcessingComponent) {
                buffer.writeVarInt(value.type.id)
            }

            override fun decode(buffer: BytesBuffer): MapPostProcessingComponent =
                MapPostProcessingComponent(MapPostProcessingType.fromID(buffer.readVarInt()))
        }
    }

    public data class ChargedProjectilesComponent(val projectiles: List<ItemStack>) : DataComponent {
        internal companion object Codec : PacketCodec<ChargedProjectilesComponent> {
            override fun encode(buffer: BytesBuffer, value: ChargedProjectilesComponent) {
                buffer.writePrefixed(value.projectiles) { writeItemStack(it) }
            }

            override fun decode(buffer: BytesBuffer): ChargedProjectilesComponent =
                ChargedProjectilesComponent(buffer.readPrefixed { readItemStack() })
        }
    }

    public data class BundleContentsComponent(val items: List<ItemStack>) : DataComponent {
        internal companion object Codec : PacketCodec<BundleContentsComponent> {
            override fun encode(buffer: BytesBuffer, value: BundleContentsComponent) {
                buffer.writePrefixed(value.items) { writeItemStack(it) }
            }

            override fun decode(buffer: BytesBuffer): BundleContentsComponent =
                BundleContentsComponent(buffer.readPrefixed { readItemStack() })
        }
    }

    public data class PotionContentsComponent(
        val potionId: Int?,
        val customColor: Int?,
        val customEffects: List<PotionEffect>,
        /**
         * it will always be null ???
         * */
        val customName: String?,
    ) : DataComponent {
        internal companion object Codec : PacketCodec<PotionContentsComponent> {
            override fun encode(buffer: BytesBuffer, value: PotionContentsComponent) {
                buffer.writePrefixedOptional(value.potionId) { writeVarInt(it) }
                buffer.writePrefixedOptional(value.customColor) { writeInt(it) }
                buffer.writePrefixed(value.customEffects) { writePotionEffect(it) }
//                buffer.writePrefixedOptional(value.customName) { writeMcString(it) }  // ????
            }

            override fun decode(buffer: BytesBuffer): PotionContentsComponent {
                val potionId = buffer.readPrefixOptional { readVarInt() }
                val customColor = buffer.readPrefixOptional { readInt() }
                val customEffects = buffer.readPrefixed { readPotionEffect() }
//                val customName = buffer.readPrefixOptional { readMcString() }  // ????
                return PotionContentsComponent(potionId, customColor, customEffects, null)
            }
        }
    }

    public data class PotionDurationScaleComponent(val effectMultiplier: Float) : DataComponent {
        internal companion object Codec : PacketCodec<PotionDurationScaleComponent> {
            override fun encode(buffer: BytesBuffer, value: PotionDurationScaleComponent) {
                buffer.writeFloat(value.effectMultiplier)
            }

            override fun decode(buffer: BytesBuffer): PotionDurationScaleComponent =
                PotionDurationScaleComponent(buffer.readFloat())
        }
    }

    public data class SuspiciousStewEffectsComponent(val effects: List<SuspiciousStewEffect>) : DataComponent {
        internal companion object Codec : PacketCodec<SuspiciousStewEffectsComponent> {
            override fun encode(buffer: BytesBuffer, value: SuspiciousStewEffectsComponent) {
                buffer.writePrefixed(value.effects) {
                    writeVarInt(it.effectId)
                    writeVarInt(it.duration)
                }
            }

            override fun decode(buffer: BytesBuffer): SuspiciousStewEffectsComponent =
                SuspiciousStewEffectsComponent(buffer.readPrefixed {
                    val effectId = readVarInt()
                    val duration = readVarInt()
                    SuspiciousStewEffect(effectId, duration)
                })
        }
    }

    public data class WritableBookContentComponent(val pages: List<WritablePage>) : DataComponent {
        internal companion object Codec : PacketCodec<WritableBookContentComponent> {
            override fun encode(buffer: BytesBuffer, value: WritableBookContentComponent) {
                buffer.writePrefixed(value.pages) {
                    writeMcString(it.rawContent)
                    writePrefixedOptional(it.filteredContent) { filtered -> writeMcString(filtered) }
                }
            }

            override fun decode(buffer: BytesBuffer): WritableBookContentComponent =
                WritableBookContentComponent(buffer.readPrefixed {
                    val rawContent = readMcString()
                    val filtered = readPrefixOptional { readMcString() }
                    WritablePage(rawContent, filtered)
                })
        }
    }

    public data class WrittenBookContentComponent(
        val rawTitle: String,
        val filteredTitle: String?,
        val author: String,
        val generation: Int,
        val pages: List<WrittenPage>,
        val resolved: Boolean,
    ) : DataComponent {
        internal companion object Codec : PacketCodec<WrittenBookContentComponent> {
            override fun encode(buffer: BytesBuffer, value: WrittenBookContentComponent) {
                buffer.writeMcString(value.rawTitle)
                buffer.writePrefixedOptional(value.filteredTitle) { writeMcString(it) }
                buffer.writeMcString(value.author)
                buffer.writeVarInt(value.generation)
                buffer.writePrefixed(value.pages) { page ->
                    writeTextComponent(page.rawContent)
                    writePrefixedOptional(page.filteredContent) { filtered -> writeTextComponent(filtered) }
                }
                buffer.writeBoolean(value.resolved)
            }

            override fun decode(buffer: BytesBuffer): WrittenBookContentComponent {
                val rawTitle = buffer.readMcString()
                val filteredTitle = buffer.readPrefixOptional { readMcString() }
                val author = buffer.readMcString()
                val generation = buffer.readVarInt()
                val pages = buffer.readPrefixed {
                    val rawContent = readTextComponent()
                    val filtered = readPrefixOptional { readTextComponent() }
                    WrittenPage(rawContent, filtered)
                }
                val resolved = buffer.readBoolean()
                return WrittenBookContentComponent(rawTitle, filteredTitle, author, generation, pages, resolved)
            }
        }
    }

    public data class TrimComponent(val material: IdOrX<TrimMaterial>, val pattern: IdOrX<TrimPattern>) :
        DataComponent {
        internal companion object Codec : PacketCodec<TrimComponent> {
            override fun encode(buffer: BytesBuffer, value: TrimComponent) {
                buffer.writeIdOrX(value.material) { material ->
                    writeMcString(material.suffix)
                    writePrefixed(material.overrides) {
                        writeIdentifier(it.armorMaterialType)
                        writeMcString(it.overriddenAssetName)
                    }
                    writeTextComponent(material.description)
                }
            }

            override fun decode(buffer: BytesBuffer): TrimComponent {
                val material = buffer.readIdOrX {
                    val suffix = readMcString()
                    val overrides = readPrefixed {
                        val armorMaterialType = readIdentifier()
                        val overriddenAssetName = readMcString()
                        TrimMaterialOverride(armorMaterialType, overriddenAssetName)
                    }
                    val description = readTextComponent()
                    TrimMaterial(suffix, overrides, description)
                }
                val pattern = buffer.readIdOrX {
                    val assetName = readMcString()
                    val templateItem = readVarInt()
                    val description = readTextComponent()
                    val decal = readBoolean()
                    TrimPattern(assetName, templateItem, description, decal)
                }
                return TrimComponent(material, pattern)
            }
        }
    }

    public data class DebugStickStateComponent(val data: NBTCompound) : DataComponent {
        internal companion object Codec : PacketCodec<DebugStickStateComponent> {
            override fun encode(buffer: BytesBuffer, value: DebugStickStateComponent) {
                buffer.writeNetworkNBTCompound(value.data)
            }

            override fun decode(buffer: BytesBuffer): DebugStickStateComponent =
                DebugStickStateComponent(buffer.readNetworkNBTCompound())
        }
    }

    public data class EntityDataComponent(val entityType: Int, val data: NBTCompound) : DataComponent {
        internal companion object Codec : PacketCodec<EntityDataComponent> {
            override fun encode(buffer: BytesBuffer, value: EntityDataComponent) {
                buffer.writeVarInt(value.entityType)
                buffer.writeNetworkNBTCompound(value.data)
            }

            override fun decode(buffer: BytesBuffer): EntityDataComponent {
                val type = buffer.readVarInt()
                val data = buffer.readNetworkNBTCompound()
                return EntityDataComponent(type, data)
            }
        }
    }

    public data class BucketEntityDataComponent(val data: NBTCompound) : DataComponent {
        internal companion object Codec : PacketCodec<BucketEntityDataComponent> {
            override fun encode(buffer: BytesBuffer, value: BucketEntityDataComponent) {
                buffer.writeNetworkNBTCompound(value.data)
            }

            override fun decode(buffer: BytesBuffer): BucketEntityDataComponent =
                BucketEntityDataComponent(buffer.readNetworkNBTCompound())
        }
    }

    public data class BlockEntityDataComponent(val type: Int, val data: NBTCompound) : DataComponent {
        internal companion object Codec : PacketCodec<BlockEntityDataComponent> {
            override fun encode(buffer: BytesBuffer, value: BlockEntityDataComponent) {
                buffer.writeVarInt(value.type)
                buffer.writeNetworkNBTCompound(value.data)
            }

            override fun decode(buffer: BytesBuffer): BlockEntityDataComponent {
                val type = buffer.readVarInt()
                val data = buffer.readNetworkNBTCompound()
                return BlockEntityDataComponent(type, data)
            }
        }
    }

    public data class InstrumentComponent(val instrument: IdOrX<InstrumentAttribute>) : DataComponent {
        internal companion object Codec : PacketCodec<InstrumentComponent> {
            override fun encode(buffer: BytesBuffer, value: InstrumentComponent) {
                buffer.writeIdOrX(value.instrument) { instrument ->
                    writeIdOrX(instrument.sound) { writeSoundEvent(it) }
                    writeFloat(instrument.useDuration)
                    writeFloat(instrument.range)
                    writeTextComponent(instrument.description)
                }
            }

            override fun decode(buffer: BytesBuffer): InstrumentComponent =
                InstrumentComponent(buffer.readIdOrX {
                    val sound = readIdOrX { readSoundEvent() }
                    val duration = readFloat()
                    val range = readFloat()
                    val description = readTextComponent()
                    InstrumentAttribute(sound, duration, range, description)
                })
        }
    }

    public data class PiercingWeaponComponent(
        val dealsKnockback: Boolean,
        val dismounts: Boolean,
        val sound: SoundEvent?,
        val hitSound: SoundEvent?,
    ) : DataComponent {
        internal companion object Codec : PacketCodec<PiercingWeaponComponent> {
            override fun encode(buffer: BytesBuffer, value: PiercingWeaponComponent) {
                buffer.writeBoolean(value.dealsKnockback)
                buffer.writeBoolean(value.dismounts)
                buffer.writePrefixedOptional(value.sound) { writeSoundEvent(it) }
                buffer.writePrefixedOptional(value.hitSound) { writeSoundEvent(it) }
            }

            override fun decode(buffer: BytesBuffer): PiercingWeaponComponent {
                val dealsKnockback = buffer.readBoolean()
                val dismounts = buffer.readBoolean()
                val soundEvent = buffer.readPrefixOptional { readSoundEvent() }
                val hitSound = buffer.readPrefixOptional { readSoundEvent() }
                return PiercingWeaponComponent(dealsKnockback, dismounts, soundEvent, hitSound)
            }
        }
    }

    public data class KineticWeaponComponent(
        val contactCooldownTicks: Int,
        val delayTicks: Int,
        val dismountConditions: KineticWeaponCondition?,
        val knockbackConditions: KineticWeaponCondition?,
        val damageConditions: KineticWeaponCondition?,
        val forwardMovement: Float,
        val damageMultiplier: Float,
        val sound: SoundEvent?,
        val hitSound: SoundEvent?,
    ) : DataComponent {
        internal companion object Codec : PacketCodec<KineticWeaponComponent> {
            override fun encode(buffer: BytesBuffer, value: KineticWeaponComponent) {
                buffer.writeVarInt(value.contactCooldownTicks)
                buffer.writeVarInt(value.delayTicks)
                buffer.writePrefixedOptional(value.dismountConditions) { writeKineticWeaponCondition(it) }
                buffer.writePrefixedOptional(value.knockbackConditions) { writeKineticWeaponCondition(it) }
                buffer.writePrefixedOptional(value.damageConditions) { writeKineticWeaponCondition(it) }
                buffer.writeFloat(value.forwardMovement)
                buffer.writeFloat(value.damageMultiplier)
                buffer.writePrefixedOptional(value.sound) { writeSoundEvent(it) }
                buffer.writePrefixedOptional(value.hitSound) { writeSoundEvent(it) }
            }

            override fun decode(buffer: BytesBuffer): KineticWeaponComponent {
                val contactCooldownTicks = buffer.readVarInt()
                val delayTicks = buffer.readVarInt()
                val dismountConditions = buffer.readPrefixOptional { readKineticWeaponCondition() }
                val knockbackConditions = buffer.readPrefixOptional { readKineticWeaponCondition() }
                val damageConditions = buffer.readPrefixOptional { readKineticWeaponCondition() }
                val forwardMovement = buffer.readFloat()
                val damageMultiplier = buffer.readFloat()
                val sound = buffer.readPrefixOptional { readSoundEvent() }
                val hitSound = buffer.readPrefixOptional { readSoundEvent() }
                return KineticWeaponComponent(
                    contactCooldownTicks, delayTicks, dismountConditions,
                    knockbackConditions, damageConditions, forwardMovement,
                    damageMultiplier, sound, hitSound
                )
            }
        }
    }

    public data class SwingAnimationComponent(val type: SwingAnimationType, val duration: Int) : DataComponent {
        internal companion object Codec : PacketCodec<SwingAnimationComponent> {
            override fun encode(buffer: BytesBuffer, value: SwingAnimationComponent) {
                buffer.writeVarInt(value.type.id)
            }

            override fun decode(buffer: BytesBuffer): SwingAnimationComponent {
                val type = SwingAnimationType.fromID(buffer.readVarInt())
                val duration = buffer.readVarInt()
                return SwingAnimationComponent(type, duration)
            }
        }
    }

    public data class AdditionalTradeCostComponent(val cost: Int) : DataComponent {
        internal companion object Codec : PacketCodec<AdditionalTradeCostComponent> {
            override fun encode(buffer: BytesBuffer, value: AdditionalTradeCostComponent) {
                buffer.writeVarInt(value.cost)
            }

            override fun decode(buffer: BytesBuffer): AdditionalTradeCostComponent =
                AdditionalTradeCostComponent(buffer.readVarInt())
        }
    }

    public data class DyeComponent(val color: DyeColor) : DataComponent {
        internal companion object Codec : PacketCodec<DyeComponent> {
            override fun encode(buffer: BytesBuffer, value: DyeComponent) {
                buffer.writeVarInt(value.color.id)
            }

            override fun decode(buffer: BytesBuffer): DyeComponent =
                DyeComponent(DyeColor.fromID(buffer.readVarInt()))
        }
    }

    public data class ProvidesTrimMaterialComponent(val material: IdOrX<TrimMaterial>) : DataComponent {
        internal companion object Codec : PacketCodec<ProvidesTrimMaterialComponent> {
            override fun encode(buffer: BytesBuffer, value: ProvidesTrimMaterialComponent) {
                buffer.writeIdOrX(value.material) { material ->
                    writeMcString(material.suffix)
                    writePrefixed(material.overrides) { override ->
                        writeIdentifier(override.armorMaterialType)
                        writeMcString(override.overriddenAssetName)
                    }
                    writeTextComponent(material.description)
                }
            }

            override fun decode(buffer: BytesBuffer): ProvidesTrimMaterialComponent =
                ProvidesTrimMaterialComponent(buffer.readIdOrX {
                    val suffix = readMcString()
                    val overrides = readPrefixed {
                        val armorMaterialType = readIdentifier()
                        val overriddenAssetName = readMcString()
                        TrimMaterialOverride(armorMaterialType, overriddenAssetName)
                    }
                    val description = readTextComponent()
                    TrimMaterial(suffix, overrides, description)
                })
        }
    }

    public data class OminousBottleAmplifierComponent(val amplifier: Int) : DataComponent {
        internal companion object Codec : PacketCodec<OminousBottleAmplifierComponent> {
            override fun encode(buffer: BytesBuffer, value: OminousBottleAmplifierComponent) {
                buffer.writeVarInt(value.amplifier)
            }

            override fun decode(buffer: BytesBuffer): OminousBottleAmplifierComponent =
                OminousBottleAmplifierComponent(buffer.readVarInt())
        }
    }

    public data class JukeboxPlayableComponent(val jukeboxSong: IdOrX<JukeboxSong>) : DataComponent {
        internal companion object Codec : PacketCodec<JukeboxPlayableComponent> {
            override fun encode(buffer: BytesBuffer, value: JukeboxPlayableComponent) {
                buffer.writeIdOrX(value.jukeboxSong) { song ->
                    writeIdOrX(song.sound) { writeSoundEvent(it) }
                    writeTextComponent(song.description)
                    writeFloat(song.duration)
                    writeVarInt(song.output)
                }
            }

            override fun decode(buffer: BytesBuffer): JukeboxPlayableComponent =
                JukeboxPlayableComponent(buffer.readIdOrX {
                    val sound = buffer.readIdOrX { readSoundEvent() }
                    val description = buffer.readTextComponent()
                    val duration = buffer.readFloat()
                    val output = buffer.readVarInt()
                    JukeboxSong(sound, description, duration, output)
                })
        }
    }

    public data class ProvidesBannerPatternsComponent(val key: IdSet) : DataComponent {
        internal companion object Codec : PacketCodec<ProvidesBannerPatternsComponent> {
            override fun encode(buffer: BytesBuffer, value: ProvidesBannerPatternsComponent) {
                buffer.writeIdSet(value.key)
            }

            override fun decode(buffer: BytesBuffer): ProvidesBannerPatternsComponent =
                ProvidesBannerPatternsComponent(buffer.readIdSet())
        }
    }

    public data class RecipesComponent(val data: NBTCompound) : DataComponent {
        internal companion object Codec : PacketCodec<RecipesComponent> {
            override fun encode(buffer: BytesBuffer, value: RecipesComponent) {
                buffer.writeNetworkNBTCompound(value.data)
            }

            override fun decode(buffer: BytesBuffer): RecipesComponent =
                RecipesComponent(buffer.readNetworkNBTCompound())
        }
    }

    public data class LodestoneTrackerComponent(
        val hasGlobalPosition: Boolean,
        val dimension: Identifier?,
        val position: BlockPos?,
        val tracked: Boolean,
    ) : DataComponent {
        internal companion object Codec : PacketCodec<LodestoneTrackerComponent> {
            override fun encode(buffer: BytesBuffer, value: LodestoneTrackerComponent) {
                buffer.writeBoolean(value.hasGlobalPosition)
                if (value.hasGlobalPosition) buffer.writeIdentifier(value.dimension!!)
                if (value.hasGlobalPosition) buffer.writeBlockPos(value.position!!)
                buffer.writeBoolean(value.tracked)
            }

            override fun decode(buffer: BytesBuffer): LodestoneTrackerComponent {
                val hasGlobalPosition = buffer.readBoolean()
                val dimension = if (hasGlobalPosition) buffer.readIdentifier() else null
                val position = if (hasGlobalPosition) buffer.readBlockPos() else null
                val tracked = buffer.readBoolean()
                return LodestoneTrackerComponent(hasGlobalPosition, dimension, position, tracked)
            }
        }
    }

    public data class FireworkExplosionComponent(val explosion: FireworkExplosionAttribute) : DataComponent {
        internal companion object Codec : PacketCodec<FireworkExplosionComponent> {
            override fun encode(buffer: BytesBuffer, value: FireworkExplosionComponent) {
                buffer.writeFireworkExplosion(value.explosion)
            }

            override fun decode(buffer: BytesBuffer): FireworkExplosionComponent =
                FireworkExplosionComponent(buffer.readFireworkExplosion())
        }
    }

    public data class FireworksComponent(val flightDuration: Int, val explosions: List<FireworkExplosionAttribute>) :
        DataComponent {
        internal companion object Codec : PacketCodec<FireworksComponent> {
            override fun encode(buffer: BytesBuffer, value: FireworksComponent) {
                buffer.writeVarInt(value.flightDuration)
                buffer.writePrefixed(value.explosions) { writeFireworkExplosion(it) }
            }

            override fun decode(buffer: BytesBuffer): FireworksComponent {
                val duration = buffer.readVarInt()
                val explosions = buffer.readPrefixed { readFireworkExplosion() }
                return FireworksComponent(duration, explosions)
            }
        }
    }

    public data class ProfileComponent(val profile: ResolvableProfile) : DataComponent {
        internal companion object Codec : PacketCodec<ProfileComponent> {
            override fun encode(buffer: BytesBuffer, value: ProfileComponent) {
                buffer.writeResolvableProfile(value.profile)
            }

            override fun decode(buffer: BytesBuffer): ProfileComponent =
                ProfileComponent(buffer.readResolvableProfile())
        }
    }

    public data class NoteBlockSoundComponent(val sound: Identifier) : DataComponent {
        internal companion object Codec : PacketCodec<NoteBlockSoundComponent> {
            override fun encode(buffer: BytesBuffer, value: NoteBlockSoundComponent) {
                buffer.writeIdentifier(value.sound)
            }

            override fun decode(buffer: BytesBuffer): NoteBlockSoundComponent =
                NoteBlockSoundComponent(buffer.readIdentifier())
        }
    }

    public data class BannerPatternsComponent(val layers: List<BannerPatternLayer>) : DataComponent {
        internal companion object Codec : PacketCodec<BannerPatternsComponent> {
            override fun encode(buffer: BytesBuffer, value: BannerPatternsComponent) {
                buffer.writePrefixed(value.layers) { writeBannerPatternLayer(it) }
            }

            override fun decode(buffer: BytesBuffer): BannerPatternsComponent =
                BannerPatternsComponent(buffer.readPrefixed { readBannerPatternLayer() })
        }
    }

    public data class BaseColorComponent(val color: DyeColor) : DataComponent {
        internal companion object Codec : PacketCodec<BaseColorComponent> {
            override fun encode(buffer: BytesBuffer, value: BaseColorComponent) {
                buffer.writeVarInt(value.color.id)
            }

            override fun decode(buffer: BytesBuffer): BaseColorComponent =
                BaseColorComponent(DyeColor.fromID(buffer.readVarInt()))
        }
    }

    public data class PotDecorationsComponent(val decorations: List<Int>) : DataComponent {
        internal companion object Codec : PacketCodec<PotDecorationsComponent> {
            override fun encode(buffer: BytesBuffer, value: PotDecorationsComponent) {
                buffer.writePrefixed(value.decorations) { writeVarInt(it) }
            }

            override fun decode(buffer: BytesBuffer): PotDecorationsComponent =
                PotDecorationsComponent(buffer.readPrefixed { readVarInt() })
        }
    }

    public data class ContainerComponent(val items: List<ItemStack>) : DataComponent {
        internal companion object Codec : PacketCodec<ContainerComponent> {
            override fun encode(buffer: BytesBuffer, value: ContainerComponent) {
                buffer.writePrefixed(value.items) { writeItemStack(it) }
            }

            override fun decode(buffer: BytesBuffer): ContainerComponent {
                return ContainerComponent(buffer.readPrefixed { readItemStack() })
            }
        }
    }

    public data class BlockStateComponent(val properties: List<BlockStateProperty>) : DataComponent {
        internal companion object Codec : PacketCodec<BlockStateComponent> {
            override fun encode(buffer: BytesBuffer, value: BlockStateComponent) {
                buffer.writePrefixed(value.properties) {
                    writeMcString(it.name)
                    writeMcString(it.value)
                }
            }

            override fun decode(buffer: BytesBuffer): BlockStateComponent = BlockStateComponent(buffer.readPrefixed {
                val name = readMcString()
                val value = readMcString()
                BlockStateProperty(name, value)
            })
        }
    }

    public data class BeesComponent(val bees: List<BeeAttribute>) : DataComponent {
        internal companion object Codec : PacketCodec<BeesComponent> {
            override fun encode(buffer: BytesBuffer, value: BeesComponent) {
                buffer.writePrefixed(value.bees) { bee ->
                    writeVarInt(bee.entityType)
                    writeNetworkNBTCompound(bee.entityData)
                    writeVarInt(bee.ticksInHive)
                    writeVarInt(bee.minTicksInHive)
                }
            }

            override fun decode(buffer: BytesBuffer): BeesComponent =
                BeesComponent(buffer.readPrefixed {
                    val entityType = readVarInt()
                    val entityData = readNetworkNBTCompound()
                    val ticksInHive = readVarInt()
                    val minTicksInHive = readVarInt()
                    BeeAttribute(entityType, entityData, ticksInHive, minTicksInHive)
                })
        }
    }

    public data class LockComponent(val key: NBTCompound) : DataComponent {
        internal companion object Codec : PacketCodec<LockComponent> {
            override fun encode(buffer: BytesBuffer, value: LockComponent) {
                buffer.writeNetworkNBTCompound(value.key)
            }

            override fun decode(buffer: BytesBuffer): LockComponent =
                LockComponent(buffer.readNetworkNBTCompound())
        }
    }

    public data class ContainerLootComponent(val data: NBTCompound) : DataComponent {
        internal companion object Codec : PacketCodec<ContainerLootComponent> {
            override fun encode(buffer: BytesBuffer, value: ContainerLootComponent) {
                buffer.writeNetworkNBTCompound(value.data)
            }

            override fun decode(buffer: BytesBuffer): ContainerLootComponent =
                ContainerLootComponent(buffer.readNetworkNBTCompound())
        }
    }

    public data class BreakSoundComponent(val sound: IdOrX<SoundEvent>) : DataComponent {
        internal companion object Codec : PacketCodec<BreakSoundComponent> {
            override fun encode(buffer: BytesBuffer, value: BreakSoundComponent) {
                buffer.writeIdOrX(value.sound) { writeSoundEvent(it) }
            }

            override fun decode(buffer: BytesBuffer): BreakSoundComponent =
                BreakSoundComponent(buffer.readIdOrX { readSoundEvent() })
        }
    }

    public data class SulfurCubeContentComponent(val content: ItemStack) : DataComponent {
        internal companion object Codec : PacketCodec<SulfurCubeContentComponent> {
            override fun encode(buffer: BytesBuffer, value: SulfurCubeContentComponent) {
                buffer.writeItemStack(value.content)
            }

            override fun decode(buffer: BytesBuffer): SulfurCubeContentComponent =
                SulfurCubeContentComponent(buffer.readItemStack())
        }
    }

    public data class VillagerVariantComponent(val variant: Int) : DataComponent {
        internal companion object Codec : PacketCodec<VillagerVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: VillagerVariantComponent) {
                buffer.writeVarInt(value.variant)
            }

            override fun decode(buffer: BytesBuffer): VillagerVariantComponent =
                VillagerVariantComponent(buffer.readVarInt())
        }
    }

    public data class WolfVariantComponent(val variant: Int) : DataComponent {
        internal companion object Codec : PacketCodec<WolfVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: WolfVariantComponent) {
                buffer.writeVarInt(value.variant)
            }

            override fun decode(buffer: BytesBuffer): WolfVariantComponent =
                WolfVariantComponent(buffer.readVarInt())
        }
    }

    public data class WolfSoundVariantComponent(val variant: Int) : DataComponent {
        internal companion object Codec : PacketCodec<WolfSoundVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: WolfSoundVariantComponent) {
                buffer.writeVarInt(value.variant)
            }

            override fun decode(buffer: BytesBuffer): WolfSoundVariantComponent =
                WolfSoundVariantComponent(buffer.readVarInt())
        }
    }

    public data class WolfCollarComponent(val color: DyeColor) : DataComponent {
        internal companion object Codec : PacketCodec<WolfCollarComponent> {
            override fun encode(buffer: BytesBuffer, value: WolfCollarComponent) {
                buffer.writeVarInt(value.color.id)
            }

            override fun decode(buffer: BytesBuffer): WolfCollarComponent =
                WolfCollarComponent(DyeColor.fromID(buffer.readVarInt()))
        }
    }

    public data class FoxVariantComponent(val variant: FoxVariantType) : DataComponent {
        internal companion object Codec : PacketCodec<FoxVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: FoxVariantComponent) {
                buffer.writeVarInt(value.variant.id)
            }

            override fun decode(buffer: BytesBuffer): FoxVariantComponent =
                FoxVariantComponent(FoxVariantType.fromID(buffer.readVarInt()))
        }
    }

    public data class SalmonSizeComponent(val type: SalmonSizeType) : DataComponent {
        internal companion object Codec : PacketCodec<SalmonSizeComponent> {
            override fun encode(buffer: BytesBuffer, value: SalmonSizeComponent) {
                buffer.writeVarInt(value.type.id)
            }

            override fun decode(buffer: BytesBuffer): SalmonSizeComponent =
                SalmonSizeComponent(SalmonSizeType.fromID(buffer.readVarInt()))
        }
    }

    public data class ParrotVariantComponent(val variant: ParrotVariantType) : DataComponent {
        internal companion object Codec : PacketCodec<ParrotVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: ParrotVariantComponent) {
                buffer.writeVarInt(value.variant.id)
            }

            override fun decode(buffer: BytesBuffer): ParrotVariantComponent =
                ParrotVariantComponent(ParrotVariantType.fromID(buffer.readVarInt()))
        }
    }

    public data class TropicalFishPatternComponent(val pattern: TropicalFishPatternType) : DataComponent {
        internal companion object Codec : PacketCodec<TropicalFishPatternComponent> {
            override fun encode(buffer: BytesBuffer, value: TropicalFishPatternComponent) {
                buffer.writeVarInt(value.pattern.id)
            }

            override fun decode(buffer: BytesBuffer): TropicalFishPatternComponent =
                TropicalFishPatternComponent(TropicalFishPatternType.fromID(buffer.readVarInt()))
        }
    }

    public data class TropicalFishBaseColorComponent(val color: DyeColor) : DataComponent {
        internal companion object Codec : PacketCodec<TropicalFishBaseColorComponent> {
            override fun encode(buffer: BytesBuffer, value: TropicalFishBaseColorComponent) {
                buffer.writeVarInt(value.color.id)
            }

            override fun decode(buffer: BytesBuffer): TropicalFishBaseColorComponent =
                TropicalFishBaseColorComponent(DyeColor.fromID(buffer.readVarInt()))
        }
    }

    public data class TropicalFishPatternColorComponent(val color: DyeColor) : DataComponent {
        internal companion object Codec : PacketCodec<TropicalFishPatternColorComponent> {
            override fun encode(buffer: BytesBuffer, value: TropicalFishPatternColorComponent) {
                buffer.writeVarInt(value.color.id)
            }

            override fun decode(buffer: BytesBuffer): TropicalFishPatternColorComponent =
                TropicalFishPatternColorComponent(DyeColor.fromID(buffer.readVarInt()))
        }
    }

    public data class MooshroomVariantComponent(val variant: MooshroomVariantType) : DataComponent {
        internal companion object Codec : PacketCodec<MooshroomVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: MooshroomVariantComponent) {
                buffer.writeVarInt(value.variant.id)
            }

            override fun decode(buffer: BytesBuffer): MooshroomVariantComponent =
                MooshroomVariantComponent(MooshroomVariantType.fromID(buffer.readVarInt()))
        }
    }

    public data class RabbitVariantComponent(val variant: RabbitVariantType) : DataComponent {
        internal companion object Codec : PacketCodec<RabbitVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: RabbitVariantComponent) {
                buffer.writeVarInt(value.variant.id)
            }

            override fun decode(buffer: BytesBuffer): RabbitVariantComponent =
                RabbitVariantComponent(RabbitVariantType.fromID(buffer.readVarInt()))
        }
    }

    public data class PigVariantComponent(val variant: Int) : DataComponent {
        internal companion object Codec : PacketCodec<PigVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: PigVariantComponent) {
                buffer.writeVarInt(value.variant)
            }

            override fun decode(buffer: BytesBuffer): PigVariantComponent =
                PigVariantComponent(buffer.readVarInt())
        }
    }

    public data class PigSoundVariantComponent(val variant: Int) : DataComponent {
        internal companion object Codec : PacketCodec<PigSoundVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: PigSoundVariantComponent) {
                buffer.writeVarInt(value.variant)
            }

            override fun decode(buffer: BytesBuffer): PigSoundVariantComponent =
                PigSoundVariantComponent(buffer.readVarInt())
        }
    }

    public data class CowVariantComponent(val variant: Int) : DataComponent {
        internal companion object Codec : PacketCodec<CowVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: CowVariantComponent) {
                buffer.writeVarInt(value.variant)
            }

            override fun decode(buffer: BytesBuffer): CowVariantComponent =
                CowVariantComponent(buffer.readVarInt())
        }
    }

    public data class CowSoundVariantComponent(val variant: Int) : DataComponent {
        internal companion object Codec : PacketCodec<CowSoundVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: CowSoundVariantComponent) {
                buffer.writeVarInt(value.variant)
            }

            override fun decode(buffer: BytesBuffer): CowSoundVariantComponent =
                CowSoundVariantComponent(buffer.readVarInt())
        }
    }

    public data class ChickenVariantComponent(val variant: Int) : DataComponent {
        internal companion object Codec : PacketCodec<ChickenVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: ChickenVariantComponent) {
                buffer.writeVarInt(value.variant)
            }

            override fun decode(buffer: BytesBuffer): ChickenVariantComponent =
                ChickenVariantComponent(buffer.readVarInt())
        }
    }

    public data class ChickenSoundVariantComponent(val variant: Int) : DataComponent {
        internal companion object Codec : PacketCodec<ChickenSoundVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: ChickenSoundVariantComponent) {
                buffer.writeVarInt(value.variant)
            }

            override fun decode(buffer: BytesBuffer): ChickenSoundVariantComponent =
                ChickenSoundVariantComponent(buffer.readVarInt())
        }
    }

    public data class FrogVariantComponent(val variant: Int) : DataComponent {
        internal companion object Codec : PacketCodec<FrogVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: FrogVariantComponent) {
                buffer.writeVarInt(value.variant)
            }

            override fun decode(buffer: BytesBuffer): FrogVariantComponent =
                FrogVariantComponent(buffer.readVarInt())
        }
    }

    public data class HorseVariantComponent(val variant: HorseVariantType) : DataComponent {
        internal companion object Codec : PacketCodec<HorseVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: HorseVariantComponent) {
                buffer.writeVarInt(value.variant.id)
            }

            override fun decode(buffer: BytesBuffer): HorseVariantComponent =
                HorseVariantComponent(HorseVariantType.fromID(buffer.readVarInt()))
        }
    }

    public data class PaintingVariantComponent(val variant: PaintingVariantType) : DataComponent {
        internal companion object Codec : PacketCodec<PaintingVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: PaintingVariantComponent) {
                buffer.writePaintingVariantType(value.variant)
            }

            override fun decode(buffer: BytesBuffer): PaintingVariantComponent =
                PaintingVariantComponent(buffer.readPaintingVariantType())
        }
    }

    public data class LlamaVariantComponent(val variant: LlamaVariantType) : DataComponent {
        internal companion object Codec : PacketCodec<LlamaVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: LlamaVariantComponent) {
                buffer.writeVarInt(value.variant.id)
            }

            override fun decode(buffer: BytesBuffer): LlamaVariantComponent =
                LlamaVariantComponent(LlamaVariantType.fromID(buffer.readVarInt()))
        }
    }

    public data class AxolotlVariantComponent(val variant: AxolotlVariantType) : DataComponent {
        internal companion object Codec : PacketCodec<AxolotlVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: AxolotlVariantComponent) {
                buffer.writeVarInt(value.variant.id)
            }

            override fun decode(buffer: BytesBuffer): AxolotlVariantComponent =
                AxolotlVariantComponent(AxolotlVariantType.fromID(buffer.readVarInt()))
        }
    }

    public data class ZombieNautilusVariantComponent(val variant: Int) : DataComponent {
        internal companion object Codec : PacketCodec<ZombieNautilusVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: ZombieNautilusVariantComponent) {
                buffer.writeVarInt(value.variant)
            }

            override fun decode(buffer: BytesBuffer): ZombieNautilusVariantComponent =
                ZombieNautilusVariantComponent(buffer.readVarInt())
        }
    }

    public data class CatVariantComponent(val variant: Int) : DataComponent {
        internal companion object Codec : PacketCodec<CatVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: CatVariantComponent) {
                buffer.writeVarInt(value.variant)
            }

            override fun decode(buffer: BytesBuffer): CatVariantComponent =
                CatVariantComponent(buffer.readVarInt())
        }
    }

    public data class CatSoundVariantComponent(val variant: Int) : DataComponent {
        internal companion object Codec : PacketCodec<CatSoundVariantComponent> {
            override fun encode(buffer: BytesBuffer, value: CatSoundVariantComponent) {
                buffer.writeVarInt(value.variant)
            }

            override fun decode(buffer: BytesBuffer): CatSoundVariantComponent =
                CatSoundVariantComponent(buffer.readVarInt())
        }
    }

    public data class CatCollarComponent(val color: DyeColor) : DataComponent {
        internal companion object Codec : PacketCodec<CatCollarComponent> {
            override fun encode(buffer: BytesBuffer, value: CatCollarComponent) {
                buffer.writeVarInt(value.color.id)
            }

            override fun decode(buffer: BytesBuffer): CatCollarComponent =
                CatCollarComponent(DyeColor.fromID(buffer.readVarInt()))
        }
    }

    public data class SheepColorComponent(val color: DyeColor) : DataComponent {
        internal companion object Codec : PacketCodec<SheepColorComponent> {
            override fun encode(buffer: BytesBuffer, value: SheepColorComponent) {
                buffer.writeVarInt(value.color.id)
            }

            override fun decode(buffer: BytesBuffer): SheepColorComponent =
                SheepColorComponent(DyeColor.fromID(buffer.readVarInt()))
        }
    }

    public data class ShulkerColorComponent(val color: DyeColor) : DataComponent {
        internal companion object Codec : PacketCodec<ShulkerColorComponent> {
            override fun encode(buffer: BytesBuffer, value: ShulkerColorComponent) {
                buffer.writeVarInt(value.color.id)
            }

            override fun decode(buffer: BytesBuffer): ShulkerColorComponent =
                ShulkerColorComponent(DyeColor.fromID(buffer.readVarInt()))
        }
    }
}