/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.protocol.game.advancement

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.readMcString
import cn.rtast.libmc.primitives.readPrefixOptional
import cn.rtast.libmc.primitives.readPrefixed
import cn.rtast.libmc.primitives.readVarInt
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.chat.readTextComponent
import cn.rtast.libmc.protocol.protocol.game.item.slot.readSlot
import cn.rtast.libmc.protocol.protocol.game.readIdentifier

public data class Advancement(
    val parentId: Identifier?,
    val displayData: AdvancementDisplay?,
    val requirements: List<List<String>>,
    val sendsTelemetryData: Boolean,
)

internal fun BytesBuffer.readAdvancement(): Advancement {
    val parentId = readPrefixOptional { readIdentifier() }
    val displayData = readPrefixOptional {
        val title = readTextComponent()
        val description = readTextComponent()
        val icon = readSlot()
        val frameType = AdvancementFrameType.fromID(readVarInt())
        val flags = readInt()
        val hasBackground = (flags and 0x01) != 0
        val backgroundTexture = if (hasBackground) readIdentifier() else null
        val x = readFloat()
        val y = readFloat()
        AdvancementDisplay(title, description, icon, frameType, flags, backgroundTexture, x, y)
    }
    val requirements = readPrefixed { readPrefixed { readMcString() } }
    val sendsTelemetryData = readBoolean()
    return Advancement(parentId, displayData, requirements, sendsTelemetryData)
}