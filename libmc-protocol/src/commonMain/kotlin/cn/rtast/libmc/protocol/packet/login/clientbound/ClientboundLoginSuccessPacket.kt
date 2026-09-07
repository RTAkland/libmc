/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package cn.rtast.libmc.protocol.packet.login.clientbound

import cn.rtast.libmc.network.BytesBuffer
import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.readUuid
import cn.rtast.libmc.protocol.protocol.game.session.GameProfile
import kotlin.uuid.Uuid

public data class ClientboundLoginSuccessPacket(val gameProfile: GameProfile, val sessionId: Uuid) :
    MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundLoginSuccessPacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundLoginSuccessPacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundLoginSuccessPacket {
            val gameProfile = GameProfile.decode(buffer)
            val sessionId = buffer.readUuid()
            return ClientboundLoginSuccessPacket(gameProfile, sessionId)
        }
    }
}