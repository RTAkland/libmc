/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package test

import cn.rtast.libmc.network.withCustom
import cn.rtast.libmc.packet.ClientboundUnknownPacket
import cn.rtast.libmc.protocol.client.createMinecraftClient
import cn.rtast.libmc.protocol.context.DefaultProtocolContext
import cn.rtast.libmc.protocol.packet.play.clientbound.ClientboundPlayerChatMessagePacket
import cn.rtast.libmc.protocol.packet.play.serverbound.ServerboundChatMessagePacket
import cn.rtast.libmc.protocol.util.generateOfflineUuid
import kotlinx.coroutines.launch
import org.junit.Test
import java.io.File
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.uuid.Uuid


class TestClientTestInJvm {
    val accessToken = File("src/jvmTest/resources/accessToken.txt").readText()
    private val chatTracker = ClientChatTracker()

    @Test
    fun `test client`() {
        val cli = createMinecraftClient(
            "127.0.0.1", 25565, "RTAkland",
            Uuid.parse("bb033844-e68e-4909-a636-1a5d1821ddc4"),
            accessToken,
            context = DefaultProtocolContext
        )
        cli.on { packet, direction -> println("$direction -> $packet") }
        cli.launch { cli.connect() }
        while (true) {
        }
    }

    @Test
    fun `test client offline mode`() {
        val cli = createMinecraftClient(
            "127.0.0.1", 25566, "11",
            generateOfflineUuid("11"), null,
            context = DefaultProtocolContext.withCustom {
                socketEngine = KtorNetworkEngine()
            }
        )

//        cli.on { packet, direction ->
//            if (packet !is ClientboundWaypointPacket)
//            println("$direction -> $packet")
//        }
        cli.onPacket<ClientboundUnknownPacket> {
            println(it)
            val snapshot = chatTracker.prepareForOutgoingMessage()
            cli.networkChannel.sendPacket(
                ServerboundChatMessagePacket(
                    "114514", Clock.System.now().toEpochMilliseconds(),
                    Random.nextLong(),
                    null, snapshot.messageCount, createAcknowledgedBitSet(snapshot.lastSeenSignatures).toByteArray(),
                    ChatPacketUtils.computePacketChecksum(snapshot.lastSeenSignatures)
                )
            )
        }
        cli.onPacket<ClientboundPlayerChatMessagePacket> {
            chatTracker.onReceivePlayerChat(it.messageSignature)
        }
//        cli.onPacket<ClientboundServerDataPacket> { println(it) }
//        cli.onPacket<ClientboundServerLinksPacket> { println(it) }
//        cli.onPacket<ClientboundCodeOfConductPacket> { println(it) }
//        cli.onPacket<ClientboundPlayerInfoUpdatePacket> { println(it) }
        cli.launch { cli.connect() }
        while (true) {
        }
    }
}