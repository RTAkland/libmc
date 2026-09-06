/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.gamerule

import cn.rtast.libmc.common.stream.BytesBuffer
import cn.rtast.libmc.common.primitives.readMcString
import cn.rtast.libmc.common.primitives.writeMcString
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier

public data class GameRuleEntry(val name: Identifier, val value: String)

internal suspend fun BytesBuffer.readGameRule(): GameRuleEntry {
    val name = readIdentifier()
    val value = readMcString()
    return GameRuleEntry(name, value)
}

internal suspend fun BytesBuffer.writeGameRule(entry: GameRuleEntry) {
    writeIdentifier(entry.name)
    writeMcString(entry.value)
}