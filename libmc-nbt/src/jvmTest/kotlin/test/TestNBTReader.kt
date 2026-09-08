/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package test

import cn.rtast.libmc.nbt.readNBTRootCompound
import cn.rtast.libmc.nbt.toNBTInput
import cn.rtast.libmc.network.wrap
import org.junit.Test
import java.io.File

class TestNBTReader {

    private val javaNBTBuffer = File("src/commonTest/resources/level.dat").readBytes().wrap()

    @Test
    fun `test read java nbt`() {
        val readRoot = javaNBTBuffer.toNBTInput().readNBTRootCompound()
        println(readRoot)
    }
}