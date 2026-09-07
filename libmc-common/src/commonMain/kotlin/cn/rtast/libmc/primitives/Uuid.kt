/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.primitives

import cn.rtast.libmc.stream.BytesBuffer
import kotlin.uuid.Uuid

public suspend fun BytesBuffer.writeUuid(uuid: Uuid): Unit = uuid.toLongs { mostSignificantBits, leastSignificantBits ->
    this.writeLong(mostSignificantBits)
    this.writeLong(leastSignificantBits)
}

public suspend fun BytesBuffer.readUuid(): Uuid {
    val most = this.readLong()
    val least = this.readLong()
    return Uuid.fromLongs(most, least)
}