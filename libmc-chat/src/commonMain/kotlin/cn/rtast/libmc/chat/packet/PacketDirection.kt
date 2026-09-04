/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.chat.packet

internal sealed interface PacketDirection {
    interface ServerboundPacket : PacketDirection
    interface ClientboundPacket : PacketDirection
    interface AcrossPacket : PacketDirection
}