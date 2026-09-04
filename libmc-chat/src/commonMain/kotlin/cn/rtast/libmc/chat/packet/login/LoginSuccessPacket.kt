/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.chat.packet.login

import cn.rtast.libmc.chat.packet.PacketDirection
import cn.rtast.libmc.chat.profile.GameProfile
import cn.rtast.libmc.common.MinecraftPacket
import cn.rtast.libmc.common.PacketCodec
import cn.rtast.libmc.common._Buffer
import cn.rtast.libmc.common.readUuid
import kotlin.uuid.Uuid

internal data class LoginSuccessPacket(
    val gameProfile: GameProfile,
    val sessionId: Uuid,
) : MinecraftPacket, PacketDirection.ClientboundPacket {
    override val packetId: Int = 0x02

    companion object Codec : PacketCodec<LoginSuccessPacket> {
        override fun encode(buffer: _Buffer, value: LoginSuccessPacket) {}
        override fun decode(buffer: _Buffer): LoginSuccessPacket {
            val gameProfile = GameProfile.decode(buffer)
            val sessionId = buffer.readUuid()
            return LoginSuccessPacket(gameProfile, sessionId)
        }
    }
}