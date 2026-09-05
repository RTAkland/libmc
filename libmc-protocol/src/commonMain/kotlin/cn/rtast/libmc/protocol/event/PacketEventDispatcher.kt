/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.event

import cn.rtast.libmc.common.packet.PacketEvent
import kotlin.reflect.KClass

public open class PacketEventDispatcher {
    @PublishedApi
    internal val eventHandlers: MutableMap<KClass<out PacketEvent>, MutableList<suspend (PacketEvent) -> Unit>> =
        mutableMapOf()

    internal suspend fun dispatch(event: PacketEvent) {
        eventHandlers[event::class]?.forEach { it.invoke(event) }
    }

    public inline fun <reified T : PacketEvent> on(crossinline block: suspend (T) -> Unit) {
        val handlers = eventHandlers.getOrPut(T::class) { mutableListOf() }
        handlers.add { event -> block(event as T) }
    }
}