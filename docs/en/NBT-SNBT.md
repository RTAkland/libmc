# NBT

```kotlin
fun main() {
    // build
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
    
    // read

    val levelDat: BytesBuffer = File("src/commonTest/resources/level.dat").readBytes().wrap()
    val readRoot = levelDat.toNBTInput().readNBTRootCompound()
    println(readRoot)
}
```

# SNBT

```kotlin
fun main() {
    // parse snbt from snbt string
    val raw = """{key1: 123,'key2': 'somevalue1',"key3": {subkey1: 0x1C8,"subkey2": "somevalue2"}}"""
    println(snbt(raw))

    // serialize snbt from NBTCompound
    println(snbt(raw).toSNBT())
    
    // build snbt
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
    println(snbt)  // SNBT String
    println(prettySnbt)  // SNBT String
    println(snbt(snbt))  // parse snbt string to NBTCompound
    println(snbt(prettySnbt))  // parse snbt string to NBTCompound
}
```