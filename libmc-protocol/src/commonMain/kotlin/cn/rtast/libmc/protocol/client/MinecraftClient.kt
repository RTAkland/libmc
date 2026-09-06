/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */

package cn.rtast.libmc.protocol.client

import cn.rtast.libmc.common.LibMCContext
import cn.rtast.libmc.protocol.event.InternalPacketDispatcher
import cn.rtast.libmc.protocol.event.PacketEventDispatcher
import cn.rtast.libmc.protocol.network.NetworkChannel
import cn.rtast.libmc.protocol.packet.handshake.ServerboundHandshakePacket
import cn.rtast.libmc.protocol.packet.login.serverbound.ServerboundLoginStartPacket
import cn.rtast.libmc.protocol.protocol.state.HandshakeIntent
import cn.rtast.libmc.protocol.protocol.state.ProtocolState
import cn.rtast.libmc.protocol.session.TransactionIdManager
import cn.rtast.libmc.protocol.util.generateOfflineUuid
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.Uuid

public class MinecraftClient internal constructor(
    private val host: String,
    private val port: Int = 25565,
    private val username: String,
    private val uuid: Uuid,
    context: LibMCContext,
    parentJob: Job?,
    private val ioDispatcher: CoroutineDispatcher,
) : PacketEventDispatcher(), CoroutineScope {

    internal val stateMachine = ClientStateMachine()
    internal val networkChannel = NetworkChannel(host, port, context, stateMachine)
    private val internalPacketDispatcher = InternalPacketDispatcher(this)

    private val clientJob = SupervisorJob(parentJob)
    private var listenJob: Job? = null

    public val transactionManager: TransactionIdManager = TransactionIdManager()

    override val coroutineContext: CoroutineContext
        get() = clientJob + ioDispatcher + CoroutineName("LibMC-MinecraftClient-$username")

    public fun connect(protocolVersion: Int = 776) {
        networkChannel.connect()
        startListening()
        networkChannel.sendPacket(
            ServerboundHandshakePacket(protocolVersion, host, port.toUShort(), HandshakeIntent.LOGIN)
        )
        stateMachine.transitionTo(ProtocolState.LOGIN)
        networkChannel.sendPacket(ServerboundLoginStartPacket(username, uuid))
    }

    private fun startListening() {
        listenJob = launch {
            try {
                while (isActive) internalPacketDispatcher.handleIncomingPackets(networkChannel.readNextPacket())
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                if (isActive) {
                    e.printStackTrace()
                    println("Network read loop exception: ${e.message}")
                    close()
                }
            }
        }
    }

    public fun close() {
        networkChannel.close()
        clientJob.cancel()
    }
}

public fun createMinecraftClient(
    host: String,
    port: Int,
    username: String,
    uuid: Uuid = generateOfflineUuid(username),
    context: LibMCContext = LibMCContext(),
    parentJob: Job? = null,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
): MinecraftClient = MinecraftClient(host, port, username, uuid, context, parentJob, ioDispatcher)