/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.event

import cn.rtast.libmc.packet.MinecraftPacket
import kotlin.reflect.KClass

public open class PacketEventDispatcher {
    @PublishedApi
    internal val eventHandlers: MutableMap<KClass<out MinecraftPacket>, MutableList<suspend (MinecraftPacket) -> Unit>> =
        mutableMapOf()

    internal suspend fun dispatch(event: MinecraftPacket) {
        eventHandlers[event::class]?.forEach { it.invoke(event) }
    }

    public inline fun <reified T : MinecraftPacket> on(crossinline block: suspend (T) -> Unit) {
        val handlers = eventHandlers.getOrPut(T::class) { mutableListOf() }
        handlers.add { event -> block(event as T) }
    }
}