/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.primitives

import cn.rtast.libmc.stream.BytesBuffer

public suspend inline fun <T> BytesBuffer.readOptional(block: BytesBuffer.() -> T): T? {
    val hasData = this.readBoolean()
    return if (hasData) block.invoke(this) else null
}

/**
 * buffer.writeOptional(value.someValue) { writeBlockPos(it) }
 */
public suspend inline fun <T> BytesBuffer.writeOptional(value: T?, block: BytesBuffer.(T) -> Unit) {
    if (value != null) {
        this.writeBoolean(true)
        block.invoke(this, value)
    } else this.writeBoolean(false)
}