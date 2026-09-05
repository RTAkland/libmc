/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.protocol.game.container

public object ContainerButton {
    public enum class Mouse(public val id: Byte) {
        LEFT(0),
        RIGHT(1)
    }

    public enum class Swap(public val id: Byte) {
        HOTBAR_1(0),
        HOTBAR_2(1),
        HOTBAR_3(2),
        HOTBAR_4(3),
        HOTBAR_5(4),
        HOTBAR_6(5),
        HOTBAR_7(6),
        HOTBAR_8(7),
        HOTBAR_9(8),
        OFFHAND(40)
    }

    public enum class Drop(public val id: Byte) {
        SINGLE_ITEM(0),
        FULL_STACK(1)
    }

    public enum class Drag(public val id: Byte) {
        START_LEFT(0),
        ADD_SLOT_LEFT(1),
        END_LEFT(2),

        START_RIGHT(4),
        ADD_SLOT_RIGHT(5),
        END_RIGHT(6),

        START_MIDDLE(8),
        ADD_SLOT_MIDDLE(9),
        END_MIDDLE(10)
    }
}