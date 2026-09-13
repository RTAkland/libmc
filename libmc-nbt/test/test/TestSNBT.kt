/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/8
 */


package test

import cn.rtast.libmc.snbt.SNBTDecoder
import cn.rtast.libmc.snbt.buildSNBT
import cn.rtast.libmc.snbt.snbt
import cn.rtast.libmc.snbt.toSNBT
import kotlin.test.Test

class TestSNBT {
    val raw = """{key1: 123,'key2': 'somevalue1',"key3": {subkey1: 0x1C8,"subkey2": "somevalue2"}}"""

    @Test
    fun `test snbt decode`() {
        println(snbt(raw))
    }

    @Test
    fun `test snbt encode`() {
        println(snbt(raw).toSNBT())
    }

    @Test
    fun `test snbt builder`() {
        val snbt = buildSNBT {
            "key1" string "TEST"
            "intValue" int 1
            "Count" byte 1
            "Damage" int 0
        }

        val prettySnbt = buildSNBT(prettyPrint = true) {
            "Name" string "Steve"
            "Health" float 20.0f
            "IsCreative" boolean true
            "Pos" intArray intArrayOf(100, 64, -200)
            "Custom Name" string "Alex\nWith Newline"
            "Attributes" compound {
                "AttackDamage" double 5.5
                "MovementSpeed" float 0.1f
            }
            "Inventory" list {
                compound {
                    "id" string "minecraft:diamond_sword"
                    "Count" byte 1
                }
                compound {
                    "id" string "minecraft:apple"
                    "Count" byte 16
                }
            }
        }
        println(snbt)
        println(prettySnbt)
        println(snbt(snbt))
        println(snbt(prettySnbt))
    }
}