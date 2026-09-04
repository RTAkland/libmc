/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.common

import kotlin.uuid.Uuid

@Suppress("CLASSNAME")
public expect class _Buffer {
    public constructor()
    public constructor(bytes: ByteArray)

    public fun writeByte(value: Byte)
    public fun writeShort(value: Short, endian: ByteOrder = ByteOrder.BIG_ENDIAN)
    public fun writeInt(value: Int, endian: ByteOrder = ByteOrder.BIG_ENDIAN)
    public fun writeLong(value: Long, endian: ByteOrder = ByteOrder.BIG_ENDIAN)
    public fun writeBytes(bytes: ByteArray)
    public fun writeBoolean(value: Boolean)

    public fun readByte(): Byte
    public fun readShort(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Short
    public fun readInt(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Int
    public fun readLong(endian: ByteOrder = ByteOrder.BIG_ENDIAN): Long
    public fun readBytes(length: Int): ByteArray
    public fun readBoolean(): Boolean

    public fun toByteArray(): ByteArray
    public fun hasRemaining(): Boolean
    public fun close()
    public val size: Int
    public val remaining: Long
}

public fun ByteArray.wrap(): _Buffer = _Buffer(this)

public fun _Buffer.writeUuid(uuid: Uuid): Unit = uuid.toLongs { mostSignificantBits, leastSignificantBits ->
    this.writeLong(mostSignificantBits)
    this.writeLong(leastSignificantBits)
}

public fun _Buffer.readUuid(): Uuid {
    val most = this.readLong()
    val least = this.readLong()
    return Uuid.fromLongs(most, least)
}

public fun _Buffer.writeVarInt(value: Int): Unit = VarIntCodec.encode(this, value)
public fun _Buffer.readVarInt(): Int = VarIntCodec.decode(this)

public fun _Buffer.writeMcString(value: String): Unit = McStringCodec.encode(this, value)
public fun _Buffer.readMcString(): String = McStringCodec.decode(this)

public fun _ReadChannel.readPacketFrame(): _Buffer {
    val length = this.readVarInt()
    val frameBytes = this.readBytes(length)
    return _Buffer().apply {
        writeBytes(frameBytes)
    }
}