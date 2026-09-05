/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package test

import cn.rtast.libmc.protocol.client.createMinecraftClient
import cn.rtast.libmc.protocol.packet.play.ClientboundDisconnectPlayPacket
import cn.rtast.libmc.protocol.packet.play.ClientboundLoginPlayPacket
import cn.rtast.libmc.protocol.packet.play.ClientboundSystemChatMessagePacket
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class TestClient {

    @Test
    fun `test client`() = runTest {
        val cli = createMinecraftClient("127.0.0.1", 25565, "123")
        cli.launch { cli.connect() }

        cli.on<ClientboundLoginPlayPacket> {
            println(it)
        }
        cli.on<ClientboundDisconnectPlayPacket> {
            println(it)
        }
        cli.on<ClientboundSystemChatMessagePacket> {
            println(it)
        }
        while (true) {
        }
    }
}