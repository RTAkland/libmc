/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/10
 */


package cn.rtast.libmc.protocol.protocol.session

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.protocol.client.CURRENT_MINECRAFT_PROTOCOL_VERSION
import cn.rtast.libmc.protocol.protocol.state.HandshakeIntent
import kotlin.reflect.KClass

public interface Session {
    @Suppress("FunctionName")
    public fun <T : SessionEvent> _onEvent(clazz: KClass<T>, block: suspend Session.(T) -> Unit)
    public suspend fun emitEvent(event: SessionEvent): Unit?
    public suspend fun login(protocolVersion: Int = CURRENT_MINECRAFT_PROTOCOL_VERSION)
    public suspend fun handshake(
        protocolVersion: Int = CURRENT_MINECRAFT_PROTOCOL_VERSION,
        intent: HandshakeIntent = HandshakeIntent.LOGIN,
    )

    public suspend fun status(protocolVersion: Int = CURRENT_MINECRAFT_PROTOCOL_VERSION): String
    public suspend fun disconnect()
    public suspend fun init()
    public suspend fun sendPacket(packet: MinecraftPacket)
}

public inline fun <reified T : SessionEvent> Session.onEvent(noinline block: suspend Session.(T) -> Unit) {
    _onEvent(T::class, block)
}