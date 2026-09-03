/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package test

import cn.rtast.mcping.mcping
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class TestPing {

    @Test
    fun `test ping java server`() = runTest {
        val response = mcping("org.mc-complex.com", 25565)
        println(response)
    }
}