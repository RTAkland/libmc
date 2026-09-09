/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */

package cn.rtast.libmc.protocol.protocol.event

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.protocol.protocol.PacketDirection
import cn.rtast.libmc.protocol.protocol.session.Session
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.reflect.KClass

private typealias Handler = suspend Session.(MinecraftPacket) -> Unit
private typealias DirectionalHandler = suspend Session.(MinecraftPacket, PacketDirection) -> Unit

public abstract class PacketEventDispatcher {
    private val mutex = Mutex()

    @PublishedApi
    internal var receiveHandlers: Map<KClass<out MinecraftPacket>, List<Handler>> = emptyMap()

    @PublishedApi
    internal var sentHandlers: Map<KClass<out MinecraftPacket>, List<Handler>> = emptyMap()

    @PublishedApi
    internal var globalHandlers: List<DirectionalHandler> = emptyList()

    @PublishedApi
    internal suspend fun <T : MinecraftPacket> addTypedHandler(
        isReceive: Boolean,
        key: KClass<T>,
        handler: Handler,
    ): ListenerRegistration {
        mutex.withLock {
            if (isReceive) {
                val current = receiveHandlers[key] ?: emptyList()
                receiveHandlers = receiveHandlers + (key to (current + handler))
            } else {
                val current = sentHandlers[key] ?: emptyList()
                sentHandlers = sentHandlers + (key to (current + handler))
            }
        }
        return ListenerRegistration { removeTypedHandler(isReceive, key, handler) }
    }

    @PublishedApi
    internal suspend fun <T : MinecraftPacket> removeTypedHandler(
        isReceive: Boolean,
        key: KClass<T>,
        handler: Handler,
    ) {
        mutex.withLock {
            if (isReceive) {
                val current = receiveHandlers[key] ?: return@withLock
                val updated = current - handler
                receiveHandlers = if (updated.isEmpty()) receiveHandlers - key else receiveHandlers + (key to updated)
            } else {
                val current = sentHandlers[key] ?: return@withLock
                val updated = current - handler
                sentHandlers = if (updated.isEmpty()) sentHandlers - key else sentHandlers + (key to updated)
            }
        }
    }

    private suspend fun dispatch(
        session: Session,
        isReceive: Boolean,
        packet: MinecraftPacket,
        direction: PacketDirection,
    ) {
        val (typeHandlers, globals) = mutex.withLock {
            val map = if (isReceive) receiveHandlers else sentHandlers
            Pair(map[packet::class], globalHandlers)
        }
        typeHandlers?.forEach { handler -> handler(session, packet) }
        globals.forEach { handler -> handler(session, packet, direction) }
    }

    internal suspend fun dispatchReceive(packet: MinecraftPacket, session: Session) =
        dispatch(session, true, packet, PacketDirection.CLIENTBOUND)

    internal suspend fun dispatchSent(packet: MinecraftPacket, session: Session) =
        dispatch(session, false, packet, PacketDirection.SERVERBOUND)

    public suspend inline fun <reified T : MinecraftPacket> onPacket(noinline block: suspend Session.(T) -> Unit): ListenerRegistration {
        val handler: Handler = { block(it as T) }
        return addTypedHandler(true, T::class, handler)
    }

    public suspend inline fun <reified T : MinecraftPacket> onSent(noinline block: suspend Session.(T) -> Unit): ListenerRegistration {
        val handler: Handler = { block(it as T) }
        return addTypedHandler(false, T::class, handler)
    }

    public suspend fun on(block: suspend Session.(packet: MinecraftPacket, direction: PacketDirection) -> Unit): ListenerRegistration {
        mutex.withLock { globalHandlers = globalHandlers + block }
        return ListenerRegistration { mutex.withLock { globalHandlers = globalHandlers - block } }
    }
}