/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package test

import cn.rtast.mcping.rconlib.rconClient
import kotlin.test.Test

class TestRCON {

    @Test
    fun `test rcon client`() {
        val host = "127.0.0.1"
        val port = 25575
        val client = rconClient(host, port)
        val authed = client.connect("123456")
        if (authed) println(client.command("list")) else throw IllegalArgumentException("incorrect password")
    }
}