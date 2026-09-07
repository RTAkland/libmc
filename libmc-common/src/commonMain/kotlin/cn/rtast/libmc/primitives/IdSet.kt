/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.primitives

import cn.rtast.libmc.stream.BytesBuffer

public sealed interface IdSet {
    public data class Tag(val tagName: String) : IdSet
    public data class Entries(val ids: List<Int>) : IdSet
}

public suspend fun BytesBuffer.readIdSet(): IdSet {
    val type = this.readVarInt()
    return if (type == 0) {
        IdSet.Tag(tagName = this.readMcString())
    } else {
        val count = type - 1
        val ids = ArrayList<Int>(count)
        (0 until count).forEach { _ -> ids.add(this.readVarInt()) }
        IdSet.Entries(ids)
    }
}

public suspend fun BytesBuffer.writeIdSet(idSet: IdSet) {
    when (idSet) {
        is IdSet.Tag -> {
            this.writeVarInt(0)
            this.writeMcString(idSet.tagName)
        }

        is IdSet.Entries -> {
            this.writeVarInt(idSet.ids.size + 1)
            for (id in idSet.ids) this.writeVarInt(id)
        }
    }
}