/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */

package test

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.protocol.client.createMinecraftClient
import cn.rtast.libmc.protocol.crypto.DefaultProtocolContext
import kotlinx.coroutines.launch
import org.junit.Test
import java.io.File
import kotlin.uuid.Uuid


class TestJvmClient {

    val accessToken = File("src/commonTest/resources/accessToken.txt").readText()

    @Test
    fun `test default protocol context`() {
        val cli = createMinecraftClient(
            "127.0.0.1",
            25565,
            "RTAkland",
//            generateOfflineUuid("RTAkland"),
            Uuid.parse("bb033844-e68e-4909-a636-1a5d1821ddc4"),
//            null,
            accessToken,
            contextBuilder = DefaultProtocolContext
        )
//        cli.on<ClientboundSystemChatMessagePacket> { println(it) }
//        cli.on<ClientboundLoginSuccessPacket> { println(it) }
        cli.onPacket<MinecraftPacket> { println(it) }
        cli.launch { cli.connect() }
        while (true) {
        }
    }
}