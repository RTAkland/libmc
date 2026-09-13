/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/6
 */


package cn.rtast.libmc.protocol.protocol.game.player.skin

public typealias SkinPartFlag = UByte

public object SkinPartFlags {
    public const val CAPE: SkinPartFlag = 0x01u
    public const val JACKET: SkinPartFlag = 0x02u
    public const val LEFT_SLEEVE: SkinPartFlag = 0x04u
    public const val RIGHT_SLEEVE: SkinPartFlag = 0x08u
    public const val RIGHT_PANTS_LEG: SkinPartFlag = 0x10u
    public const val LEFT_PANTS_LEG: SkinPartFlag = 0x20u
    public const val HAT: SkinPartFlag = 0x40u
    public const val ALL: SkinPartFlag = 0x7Fu
}