/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.game.item

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.readPrefixOptional
import cn.rtast.libmc.primitives.writePrefixedOptional
import cn.rtast.libmc.protocol.protocol.game.Identifier
import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.game.chat.readTextComponent
import cn.rtast.libmc.protocol.protocol.game.chat.writeTextComponent
import cn.rtast.libmc.protocol.protocol.game.readIdentifier
import cn.rtast.libmc.protocol.protocol.game.writeIdentifier

public data class PaintingVariantType(
    val width: Int,
    val height: Int,
    val assetId: Identifier,
    val title: TextComponent?,
    val author: TextComponent?,
)

internal fun BytesBuffer.readPaintingVariantType(): PaintingVariantType {
    val width = readInt()
    val height = readInt()
    val assetId = readIdentifier()
    val title = readPrefixOptional { readTextComponent() }
    val author = readPrefixOptional { readTextComponent() }
    return PaintingVariantType(width, height, assetId, title, author)
}

internal fun BytesBuffer.writePaintingVariantType(variant: PaintingVariantType) {
    writeInt(variant.width)
    writeInt(variant.height)
    writeIdentifier(variant.assetId)
    writePrefixedOptional(variant.title) { writeTextComponent(it) }
    writePrefixedOptional(variant.author) { writeTextComponent(it) }
}