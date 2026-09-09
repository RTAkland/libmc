/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/9
 */


package cn.rtast.libmc.protocol.protocol.game.component

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.primitives.readMcString
import cn.rtast.libmc.primitives.readOptional
import cn.rtast.libmc.primitives.writeMcString
import cn.rtast.libmc.primitives.writeOptional

public data class DataProperty(
    val name: String,
    val isExactMatch: Boolean,
    val exactValue: String?,
    val minValue: String?,
    val maxValue: String?,
)

internal fun BytesBuffer.readDataProperty(): DataProperty {
    val name = readMcString()
    val exactMatch = readBoolean()
    val exactValue = readOptional { readMcString() }
    val minValue = readOptional { readMcString() }
    val maxValue = readOptional { readMcString() }
    return DataProperty(name, exactMatch, exactValue, minValue, maxValue)
}

internal fun BytesBuffer.writeDataProperty(property: DataProperty) {
    writeMcString(property.name)
    writeBoolean(property.isExactMatch)
    writeOptional(property.exactValue) { writeMcString(it) }
    writeOptional(property.minValue) { writeMcString(it) }
    writeOptional(property.maxValue) { writeMcString(it) }
}