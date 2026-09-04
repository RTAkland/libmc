/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package test

import cn.rtast.libmc.chat.MinecraftChatClient
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.uuid.Uuid

class TestChatClient {

    @Test
    fun `test chat client`() = runTest {
        val cli = MinecraftChatClient("127.0.0.1", 25565, "RTAkland", Uuid.parse("0dc6a9e9-a6df-3f3e-ae07-e6dbdf74b294"))
        cli.start()
    }
}