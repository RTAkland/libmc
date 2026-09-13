/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.protocol.protocol.game.command

import cn.rtast.libmc.protocol.protocol.game.Identifier

public enum class CommandSuggestionsType(public val id: Identifier) {
    AskServer(Identifier.of("minecraft:ask_server")),
    AllRecipes(Identifier.of("minecraft:all_recipes")),
    AvailableSounds(Identifier.of("minecraft:available_sounds")),
    SummonableEntities(Identifier.of("minecraft:summonable_entities"));

    public companion object {
        public fun fromIdentifier(id: Identifier?): CommandSuggestionsType? =
            entries.firstOrNull { it.id.raw == id?.raw }
    }
}