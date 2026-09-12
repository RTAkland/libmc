/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */

package cn.rtast.libmc.protocol.client

import cn.rtast.libmc.context.ProtocolContext
import cn.rtast.libmc.context.ProtocolContextBuilder
import cn.rtast.libmc.protocol.network.NetworkChannel
import cn.rtast.libmc.protocol.protocol.event.PacketEventDispatcher
import cn.rtast.libmc.protocol.protocol.session.Session
import cn.rtast.libmc.protocol.protocol.session.SessionEvent
import cn.rtast.libmc.protocol.protocol.session.SessionImpl
import cn.rtast.libmc.protocol.threads.createFixedThreadPool
import cn.rtast.libmc.protocol.util.TransactionIdManager
import cn.rtast.libmc.protocol.util.generateOfflineUuid
import kotlin.uuid.Uuid

public class MinecraftClient internal constructor(
    internal val host: String,
    internal val port: Int,
    internal val username: String,
    internal val uuid: Uuid,
    internal val accessToken: String?,
    internal val protocolContext: ProtocolContext,
    public val session: SessionImpl = SessionImpl(),
) : PacketEventDispatcher(), Session by session {
    internal val stateMachine = ClientStateMachine(session)
    public val networkChannel: NetworkChannel = NetworkChannel(this)
    public val transactionManager: TransactionIdManager = TransactionIdManager()
    internal val executor = createFixedThreadPool(4)

    init {
        session.attachClient(this)
    }

    public fun connect() {
        networkChannel.connect()
        startListening()
        session.init()
        session.emitEvent(SessionEvent.ConnectedEvent)
    }

    private fun startListening() {
        executor.submit {
            try {
                while (isActive) networkChannel.readNextPacket()
            } catch (e: Throwable) {
                e.printStackTrace()
                println("Network read loop exception: ${e.message}")
            } finally {
                networkChannel.close()
            }
        }
    }

    public fun close() {
        networkChannel.close()
        executor.shutdown()
    }
}

public fun createMinecraftClient(
    host: String,
    port: Int = 25565,
    username: String,
    uuid: Uuid = generateOfflineUuid(username),
    accessToken: String? = null,
    context: ProtocolContextBuilder.() -> Unit,
): MinecraftClient {
    val context = ProtocolContextBuilder(accessToken != null).apply(context).build()
    return MinecraftClient(
        host = host,
        port = port,
        username = username,
        uuid = uuid,
        accessToken = accessToken,
        protocolContext = context
    )
}

internal const val CURRENT_MINECRAFT_PROTOCOL_VERSION: Int = 776