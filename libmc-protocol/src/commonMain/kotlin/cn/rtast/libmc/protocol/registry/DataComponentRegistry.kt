/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */

package cn.rtast.libmc.protocol.registry

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.writeVarInt
import cn.rtast.libmc.protocol.protocol.game.data.component.DataComponent
import kotlin.reflect.KClass

internal object DataComponentRegistry {
    private val codecMap = mutableMapOf<Int, PacketCodec<out DataComponent>>()
    private val classToIdMap = mutableMapOf<KClass<out DataComponent>, Int>()
    private lateinit var codecArray: Array<PacketCodec<DataComponent>>

    private fun <T : DataComponent> register(kClass: KClass<T>, codec: PacketCodec<T>) {
        val id = codecMap.size
        codecMap[id] = codec
        classToIdMap[kClass] = id
    }

    inline fun <reified T : DataComponent> register(codec: PacketCodec<T>) = register(T::class, codec)

    fun freeze() {
        val maxId = codecMap.keys.maxOrNull() ?: -1
        codecArray = Array(maxId + 1) { index ->
            @Suppress("UNCHECKED_CAST")
            codecMap[index] as? PacketCodec<DataComponent> ?: error("Missing component codec for $index")
        }
        codecMap.clear()
    }

    internal fun read(typeId: Int, buffer: BytesBuffer): DataComponent {
        val codec = codecArray.getOrNull(typeId) ?: error("Unknown DataComponent ID: $typeId")
        return codec.decode(buffer)
    }

    internal fun write(buffer: BytesBuffer, component: DataComponent) {
        val typeId = classToIdMap[component::class] ?: error("Unregistered DataComponent class: ${component::class}")
        val codec = codecArray[typeId]
        buffer.writeVarInt(typeId)
        codec.encode(buffer, component)
    }

    init {
        register(DataComponent.CustomDataComponent)
        register(DataComponent.MaxStackSizeComponent)
        register(DataComponent.MaxDamageComponent)
        register(DataComponent.DamageComponent)
        register(DataComponent.UnbreakableComponent)
        register(DataComponent.UseEffectsComponent)
        register(DataComponent.CustomNameComponent)
        register(DataComponent.MinimumAttackChargeComponent)
        register(DataComponent.DamageTypeComponent)
        register(DataComponent.ItemNameComponent)
        register(DataComponent.ItemModelComponent)
        register(DataComponent.LoreComponent)
        register(DataComponent.RarityComponent)
        register(DataComponent.EnchantmentsComponent)
        register(DataComponent.CanPlaceOnComponent)
        register(DataComponent.CanBreakComponent)
        register(DataComponent.AttributeModifiersComponent)
        register(DataComponent.CustomModelDataComponent)
        register(DataComponent.TooltipDisplayComponent)
        register(DataComponent.RepairCostComponent)
        register(DataComponent.CreativeSlotLockComponent)
        register(DataComponent.EnchantmentGlintOverrideComponent)
        register(DataComponent.IntangibleProjectileComponent)
        register(DataComponent.FoodComponent)
        register(DataComponent.ConsumableComponent)
        register(DataComponent.UseRemainderComponent)
        register(DataComponent.UseCooldownComponent)
        register(DataComponent.DamageResistantComponent)
        register(DataComponent.ToolComponent)
        register(DataComponent.WeaponComponent)
        register(DataComponent.AttackRangeComponent)
        register(DataComponent.EnchantableComponent)
        register(DataComponent.EquippableComponent)
        register(DataComponent.RepairableComponent)
        register(DataComponent.GliderComponent)
        register(DataComponent.TooltipStyleComponent)
        register(DataComponent.DeathProtectionComponent)
        register(DataComponent.BlocksAttacksComponent)
        register(DataComponent.PiercingWeaponComponent)
        register(DataComponent.KineticWeaponComponent)
        register(DataComponent.SwingAnimationComponent)
        register(DataComponent.AdditionalTradeCostComponent)
        register(DataComponent.StoredEnchantmentsComponent)
        register(DataComponent.DyeComponent)
        register(DataComponent.DyedColorComponent)
        register(DataComponent.MapColorComponent)
        register(DataComponent.MapIdComponent)
        register(DataComponent.MapDecorationsComponent)
        register(DataComponent.MapPostProcessingComponent)
        register(DataComponent.ChargedProjectilesComponent)
        register(DataComponent.BundleContentsComponent)
        register(DataComponent.PotionContentsComponent)
        register(DataComponent.PotionDurationScaleComponent)
        register(DataComponent.SuspiciousStewEffectsComponent)
        register(DataComponent.WritableBookContentComponent)
        register(DataComponent.WrittenBookContentComponent)
        register(DataComponent.TrimComponent)
        register(DataComponent.DebugStickStateComponent)
        register(DataComponent.EntityDataComponent)
        register(DataComponent.BucketEntityDataComponent)
        register(DataComponent.BlockEntityDataComponent)
        register(DataComponent.InstrumentComponent)
        register(DataComponent.ProvidesTrimMaterialComponent)
        register(DataComponent.OminousBottleAmplifierComponent)
        register(DataComponent.JukeboxPlayableComponent)
        register(DataComponent.ProvidesBannerPatternsComponent)
        register(DataComponent.RecipesComponent)
        register(DataComponent.LodestoneTrackerComponent)
        register(DataComponent.FireworkExplosionComponent)
        register(DataComponent.FireworksComponent)
        register(DataComponent.ProfileComponent)
        register(DataComponent.NoteBlockSoundComponent)
        register(DataComponent.BannerPatternsComponent)
        register(DataComponent.BaseColorComponent)
        register(DataComponent.PotDecorationsComponent)
        register(DataComponent.ContainerComponent)
        register(DataComponent.BlockStateComponent)
        register(DataComponent.BeesComponent)
        register(DataComponent.SulfurCubeContentComponent)
        register(DataComponent.LockComponent)
        register(DataComponent.ContainerLootComponent)
        register(DataComponent.BreakSoundComponent)
        register(DataComponent.VillagerVariantComponent)
        register(DataComponent.WolfVariantComponent)
        register(DataComponent.WolfSoundVariantComponent)
        register(DataComponent.WolfCollarComponent)
        register(DataComponent.FoxVariantComponent)
        register(DataComponent.SalmonSizeComponent)
        register(DataComponent.ParrotVariantComponent)
        register(DataComponent.TropicalFishPatternComponent)
        register(DataComponent.TropicalFishBaseColorComponent)
        register(DataComponent.TropicalFishPatternColorComponent)
        register(DataComponent.MooshroomVariantComponent)
        register(DataComponent.RabbitVariantComponent)
        register(DataComponent.PigVariantComponent)
        register(DataComponent.PigSoundVariantComponent)
        register(DataComponent.CowVariantComponent)
        register(DataComponent.CowSoundVariantComponent)
        register(DataComponent.ChickenVariantComponent)
        register(DataComponent.ChickenSoundVariantComponent)
        register(DataComponent.ZombieNautilusVariantComponent)
        register(DataComponent.FrogVariantComponent)
        register(DataComponent.HorseVariantComponent)
        register(DataComponent.PaintingVariantComponent)
        register(DataComponent.LlamaVariantComponent)
        register(DataComponent.AxolotlVariantComponent)
        register(DataComponent.CatVariantComponent)
        register(DataComponent.CatSoundVariantComponent)
        register(DataComponent.CatCollarComponent)
        register(DataComponent.SheepColorComponent)
        register(DataComponent.ShulkerColorComponent)

        freeze()
    }
}

internal fun BytesBuffer.readDataComponent(typeId: Int): DataComponent =
    DataComponentRegistry.read(typeId, this)

internal fun BytesBuffer.writeDataComponent(component: DataComponent) {
    DataComponentRegistry.write(this, component)
}