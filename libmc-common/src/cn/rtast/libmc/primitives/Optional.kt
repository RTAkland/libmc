/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.primitives

import cn.rtast.libmc.network.BytesBuffer

public inline fun <T> BytesBuffer.readOptional(condition: Boolean, block: BytesBuffer.() -> T): T? =
    if (condition) block() else null

public inline fun <T> BytesBuffer.writeOptional(value: T?, block: BytesBuffer.(T) -> Unit): Unit =
    if (value != null) block.invoke(this, value) else Unit
