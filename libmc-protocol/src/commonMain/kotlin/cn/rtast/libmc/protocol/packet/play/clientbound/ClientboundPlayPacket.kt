/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.common.packet.MinecraftPacket

/**
 * mark a packet that is used when join to the server
 */
public sealed interface ClientboundPlayPacket : MinecraftPacket