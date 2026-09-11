/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/11
 */


package cn.rtast.libmc.protocol.registry

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.writeVarInt
import kotlin.concurrent.Volatile
import kotlin.reflect.KClass

internal interface Registry<T : Any> {
    fun freeze()
    fun isFrozen(): Boolean
    fun getCodec(id: Int): PacketCodec<out T>?
    fun getId(kClass: KClass<out T>): Int?
}

internal open class ArrayIndexedRegistry<T : Any>(private val registryName: String = "Registry") : Registry<T> {
    private val codecMap = mutableMapOf<Int, PacketCodec<out T>>()
    private val classToIdMap = mutableMapOf<KClass<out T>, Int>()

    @Volatile
    private var frozen = false
    private lateinit var codecArray: Array<PacketCodec<T>>

    fun <U : T> register(kClass: KClass<U>, codec: PacketCodec<U>) {
        check(!frozen) { "$registryName is frozen and cannot accept new registrations." }
        val id = codecMap.size
        codecMap[id] = codec
        classToIdMap[kClass] = id
    }

    inline fun <reified U : T> register(codec: PacketCodec<U>) = register(U::class, codec)

    override fun freeze() {
        if (frozen) return
        val maxId = codecMap.keys.maxOrNull() ?: -1
        codecArray = Array(maxId + 1) { index ->
            @Suppress("UNCHECKED_CAST")
            codecMap[index] as? PacketCodec<T> ?: error("[$registryName] Missing codec for ID: $index")
        }
        codecMap.clear()
        frozen = true
    }

    override fun isFrozen(): Boolean = frozen

    override fun getCodec(id: Int): PacketCodec<T>? {
        check(frozen) { "$registryName is not frozen yet!" }
        return codecArray.getOrNull(id)
    }

    override fun getId(kClass: KClass<out T>): Int? {
        return classToIdMap[kClass]
    }

    fun read(typeId: Int, buffer: BytesBuffer): T {
        val codec = getCodec(typeId) ?: error("[$registryName] Unknown type ID: $typeId")
        return codec.decode(buffer)
    }

    fun write(buffer: BytesBuffer, value: T) {
        val kClass = value::class
        val typeId = getId(kClass) ?: error("[$registryName] Unregistered class: $kClass")
        val codec = getCodec(typeId) ?: error("[$registryName] Missing codec for frozen ID: $typeId")
        buffer.writeVarInt(typeId)
        codec.encode(buffer, value)
    }
}