/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/9
 */


package cn.rtast.libmc.protocol.protocol.game.particle

import cn.rtast.libmc.protocol.protocol.game.Identifier

public enum class ParticleType(public val id: Int, public val key: String) {
    ANGRY_VILLAGER(0, "angry_villager"), BLOCK(1, "block"), BLOCK_MARKER(2, "block_marker"),
    BUBBLE(3, "bubble"), SULFUR_BUBBLES(4, "sulfur_bubbles"), NOXIOUS_GAS(5, "noxious_gas"),
    NOXIOUS_GAS_CLOUD(6, "noxious_gas_cloud"), GEYSER(7, "geyser"), GEYSER_BASE(8, "geyser_base"),
    GEYSER_POOF(9, "geyser_poof"), GEYSER_PLUME(10, "geyser_plume"), CLOUD(11, "cloud"),
    COPPER_FIRE_FLAME(12, "copper_fire_flame"), CRIT(13, "crit"), DAMAGE_INDICATOR(14, "damage_indicator"),
    DRAGON_BREATH(15, "dragon_breath"), DRIPPING_LAVA(16, "dripping_lava"), FALLING_LAVA(17, "falling_lava"),
    LANDING_LAVA(18, "landing_lava"), DRIPPING_WATER(19, "dripping_water"), FALLING_WATER(20, "falling_water"),
    DUST(21, "dust"), DUST_COLOR_TRANSITION(22, "dust_color_transition"), EFFECT(23, "effect"),
    ELDER_GUARDIAN(24, "elder_guardian"), ENCHANTED_HIT(25, "enchanted_hit"), ENCHANT(26, "enchant"),
    END_ROD(27, "end_rod"), ENTITY_EFFECT(28, "entity_effect"), EXPLOSION_EMITTER(29, "explosion_emitter"),
    EXPLOSION(30, "explosion"), GUST(31, "gust"), SMALL_GUST(32, "small_gust"),
    GUST_EMITTER_LARGE(33, "gust_emitter_large"), GUST_EMITTER_SMALL(34, "gust_emitter_small"),
    SONIC_BOOM(35, "sonic_boom"), FALLING_DUST(36, "falling_dust"), FIREWORK(37, "firework"), FISHING(38, "fishing"),
    FLAME(39, "flame"), INFESTED(40, "infested"), CHERRY_LEAVES(41, "cherry_leaves"),
    PALE_OAK_LEAVES(42, "pale_oak_leaves"), TINTED_LEAVES(43, "tinted_leaves"), SCULK_SOUL(44, "sculk_soul"),
    SCULK_CHARGE(45, "sculk_charge"), SCULK_CHARGE_POP(46, "sculk_charge_pop"), SOUL_FIRE_FLAME(47, "soul_fire_flame"),
    SOUL(48, "soul"), FLASH(49, "flash"), HAPPY_VILLAGER(50, "happy_villager"), COMPOSTER(51, "composter"),
    HEART(52, "heart"), INSTANT_EFFECT(53, "instant_effect"), ITEM(54, "item"), VIBRATION(55, "vibration"),
    TRAIL(56, "trail"), PAUSE_MOB_GROWTH(57, "pause_mob_growth"), RESET_MOB_GROWTH(58, "reset_mob_growth"),
    ITEM_SLIME(59, "item_slime"), ITEM_COBWEB(60, "item_cobweb"), ITEM_SNOWBALL(61, "item_snowball"),
    LARGE_SMOKE(62, "large_smoke"), LAVA(63, "lava"), MYCELIUM(64, "mycelium"), NOTE(65, "note"), POOF(66, "poof"),
    PORTAL(67, "portal"), RAIN(68, "rain"), SMOKE(69, "smoke"), WHITE_SMOKE(70, "white_smoke"), SNEEZE(71, "sneeze"),
    SPIT(72, "spit"), SQUID_INK(73, "squid_ink"), SWEEP_ATTACK(74, "sweep_attack"),
    TOTEM_OF_UNDYING(75, "totem_of_undying"), UNDERWATER(76, "underwater"), SPLASH(77, "splash"), WITCH(78, "witch"),
    BUBBLE_POP(79, "bubble_pop"), CURRENT_DOWN(80, "current_down"), BUBBLE_COLUMN_UP(81, "bubble_column_up"),
    NAUTILUS(82, "nautilus"), DOLPHIN(83, "dolphin"), CAMPFIRE_COSY_SMOKE(84, "campfire_cosy_smoke"),
    CAMPFIRE_SIGNAL_SMOKE(85, "campfire_signal_smoke"), DRIPPING_HONEY(86, "dripping_honey"),
    FALLING_HONEY(87, "falling_honey"), LANDING_HONEY(88, "landing_honey"), FALLING_NECTAR(89, "falling_nectar"),
    FALLING_SPORE_BLOSSOM(90, "falling_spore_blossom"), ASH(91, "ash"), CRIMSON_SPORE(92, "crimson_spore"),
    WARPED_SPORE(93, "warped_spore"), SPORE_BLOSSOM_AIR(94, "spore_blossom_air"),
    DRIPPING_OBSIDIAN_TEAR(95, "dripping_obsidian_tear"), FALLING_OBSIDIAN_TEAR(96, "falling_obsidian_tear"),
    LANDING_OBSIDIAN_TEAR(97, "landing_obsidian_tear"), REVERSE_PORTAL(98, "reverse_portal"),
    WHITE_ASH(99, "white_ash"), SMALL_FLAME(100, "small_flame"), SNOWFLAKE(101, "snowflake"),
    DRIPPING_DRIPSTONE_LAVA(102, "dripping_dripstone_lava"), FALLING_DRIPSTONE_LAVA(103, "falling_dripstone_lava"),
    DRIPPING_DRIPSTONE_WATER(104, "dripping_dripstone_water"), FALLING_DRIPSTONE_WATER(105, "falling_dripstone_water"),
    GLOW_SQUID_INK(106, "glow_squid_ink"), GLOW(107, "glow"), WAX_ON(108, "wax_on"), WAX_OFF(109, "wax_off"),
    ELECTRIC_SPARK(110, "electric_spark"), SCRAPE(111, "scrape"), SHRIEK(112, "shriek"), EGG_CRACK(113, "egg_crack"),
    DUST_PLUME(114, "dust_plume"), TRIAL_SPAWNER_DETECTED_PLAYER(115, "trial_spawner_detection"),
    TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS(116, "trial_spawner_detection_ominous"),
    VAULT_CONNECTION(117, "vault_connection"), DUST_PILLAR(118, "dust_pillar"),
    OMINOUS_SPAWNING(119, "ominous_spawning"), RAID_OMEN(120, "raid_omen"), TRIAL_OMEN(121, "trial_omen"),
    BLOCK_CRUMBLE(122, "block_crumble"), FIREFLY(123, "firefly"), SULFUR_CUBE_GOO(124, "sulfur_cube_goo");

    public val identifier: Identifier = Identifier.of("minecraft", key)

    public companion object {
        private val CACHED_ID = entries
        public fun fromID(id: Int): ParticleType = CACHED_ID.first { it.id == id }
    }
}