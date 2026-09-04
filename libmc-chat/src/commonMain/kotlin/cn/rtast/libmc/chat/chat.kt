/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.chat

import cn.rtast.libmc.chat.packet.configuration.AckFinishConfigurationPacket
import cn.rtast.libmc.chat.packet.configuration.ServerboundPongPacket
import cn.rtast.libmc.chat.packet.configuration.ServerboundSelectKnownPacksPacket
import cn.rtast.libmc.chat.packet.handshake.HandshakePacket
import cn.rtast.libmc.chat.packet.login.LoginAcknowledgedPacket
import cn.rtast.libmc.chat.packet.login.LoginStartPacket
import cn.rtast.libmc.chat.packet.play.ServerboundKeepAlivePlayPacket
import cn.rtast.libmc.chat.protocol.HandshakeIntent
import cn.rtast.libmc.chat.protocol.ProtocolState
import cn.rtast.libmc.chat.util.generateOfflineUuid
import cn.rtast.libmc.common.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid


public class MinecraftChatClient(
    private val host: String,
    private val port: Int,
    private val username: String,
    private val uuid: Uuid = generateOfflineUuid(username),
    private val context: LibMCContext = LibMCContext(),
) {
    private var state = ProtocolState.HANDSHAKE

    public suspend fun start(): Unit = coroutineScope {
        val socket = _Socket(host, port, context)
        val input = socket.openReadChannel()
        val output = socket.openWriteChannel()

        executeInitHandshake(output)

        val readerJob = launch(Dispatchers.Default) {
            handleIncomingPackets(input, output)
        }

        readerJob.join()
    }

    private fun executeInitHandshake(output: _WriteChannel) {
        val handshakePacket = HandshakePacket(776, host, port.toUShort(), HandshakeIntent.LOGIN)
        output.sendPacket(handshakePacket, HandshakePacket)
        state = ProtocolState.LOGIN

        val loginStartPacket = LoginStartPacket(username, uuid)
        output.sendPacket(loginStartPacket, LoginStartPacket)
    }

    private suspend fun handleIncomingPackets(input: _ReadChannel, output: _WriteChannel) {
        try {
            while (currentCoroutineContext().isActive) {
                val packetLength = input.readVarInt()
                if (packetLength <= 0) continue

                val packetBytes = ByteArray(packetLength)
                input.readFully(packetBytes, 0, packetLength)

                val buffer = _Buffer(packetBytes)
                val packetId = buffer.readVarInt()
                println("received -> State: $state | ID: 0x${packetId.toString(16).uppercase()} | Length: $packetLength")
                try {
                    when (state) {
                        ProtocolState.LOGIN -> handleLoginPackets(packetId, output)
                        ProtocolState.CONFIGURATION -> handleConfigurationPackets(packetId, buffer, output)
                        ProtocolState.PLAY -> handlePlayPackets(packetId, buffer, output)
                        else -> {}
                    }
                } catch (e: Exception) {
                    println("parsing 0x${packetId.toString(16).uppercase()} Payload failed: ${e.message}")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("disconnecting: ${e.message}")
        }
    }

    private fun handleLoginPackets(packetId: Int, output: _WriteChannel) {
        when (packetId) {
            0x02 -> {
                output.sendPacket(LoginAcknowledgedPacket(), LoginAcknowledgedPacket)
                state = ProtocolState.CONFIGURATION
                println("[3/4] sent LoginAcknowledgedPacket -> switching to CONFIGURATION state")

                output.sendPacket(
                    ServerboundSelectKnownPacksPacket(knownPacks = emptyList()),
                    ServerboundSelectKnownPacksPacket
                )
            }

            0x00 -> {
                println("login denied (ClientboundDisconnectLoginPacket)")
            }
        }
    }

    private fun handleConfigurationPackets(packetId: Int, packetBuffer: _Buffer, output: _WriteChannel) {
        when (packetId) {
            0x0E -> {
                println("received ClientboundSelectKnownPacksPacket")
            }

            0x03 -> {
                output.sendPacket(AckFinishConfigurationPacket, AckFinishConfigurationPacket)
                state = ProtocolState.PLAY
            }

            0x05 -> {
                output.sendPacket(ServerboundPongPacket(0), ServerboundPongPacket)
            }

            0x01 -> println("configuration state disconnected")
        }
    }

    private fun handlePlayPackets(packetId: Int, packetBuffer: _Buffer, output: _WriteChannel) {
        try {
            when (packetId) {
                0x2B -> println("[PLAY] Joined world")

                0x2c -> {
                    val keepAliveId = packetBuffer.readLong()
                    output.sendPacket(ServerboundKeepAlivePlayPacket(id = keepAliveId), ServerboundKeepAlivePlayPacket)
                    println("[PLAY] reply keep alive packet $keepAliveId")
                }

                0x1D -> println("[PLAY] disconnected (ClientboundDisconnectPlayPacket)")
                else -> {}
            }
        } catch (e: Exception) {
            println("parsing 0x${packetId.toString(16).uppercase()} failed, skipped: ${e.message}")
        }
    }
}