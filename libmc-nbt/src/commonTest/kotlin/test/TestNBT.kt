/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/8
 */


package test

import cn.rtast.libmc.nbt.NBTTag
import cn.rtast.libmc.nbt.buildNBT
import cn.rtast.libmc.nbt.toNBTOutput
import cn.rtast.libmc.nbt.writeNBTRootCompound
import cn.rtast.libmc.network.BytesBuffer
import kotlin.test.Test

class TestNBT {
    @Test
    fun `test nbt`() {
        val nbt = buildNBT {
            "t_b" byte 0x01
            "n" compound {
                "n_s" string "STR"
                "n_d" double 0.0
                "n_f" float 0.1f
                "n_ia" intArray intArrayOf(1, 1, 1, 1)
            }
        }
        val buf = BytesBuffer().toNBTOutput(
            NBTTag.CompoundTag(
                mapOf("" to nbt)
            )
        ).writeNBTRootCompound()
        println(buf.toHexString())
    }
}