/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.protocol.packet.play.clientbound

import cn.rtast.libmc.packet.MinecraftPacket
import cn.rtast.libmc.packet.PacketCodec
import cn.rtast.libmc.primitives.*
import cn.rtast.libmc.protocol.protocol.game.chat.readTextComponent
import cn.rtast.libmc.protocol.protocol.game.player.action.PlayerInfoUpdateEntry
import cn.rtast.libmc.protocol.protocol.game.player.action.PlayerUpdateInfoAction
import cn.rtast.libmc.protocol.protocol.game.player.action.SinglePlayerAction
import cn.rtast.libmc.protocol.protocol.game.session.GameProfile
import cn.rtast.libmc.network.BytesBuffer

public data class ClientboundPlayerInfoUpdatePacket(
    val actions: Set<PlayerUpdateInfoAction>,
    val entries: List<PlayerInfoUpdateEntry>,
) : MinecraftPacket {
    internal companion object Codec : PacketCodec<ClientboundPlayerInfoUpdatePacket> {
        override fun encode(buffer: BytesBuffer, value: ClientboundPlayerInfoUpdatePacket) {}
        override fun decode(buffer: BytesBuffer): ClientboundPlayerInfoUpdatePacket {
            val actionsMask = buffer.readUByte().toInt()
            val actionsSet = PlayerUpdateInfoAction.parseActions(actionsMask)
            val playerCount = buffer.readVarInt()
            val entries = ArrayList<PlayerInfoUpdateEntry>(playerCount)
            repeat(playerCount) {
                val playerUuid = buffer.readUuid()
                val playerActions = ArrayList<SinglePlayerAction>(actionsSet.size)
                for (action in PlayerUpdateInfoAction.entries) {
                    if (action !in actionsSet) continue
                    val parsedAction = when (action) {
                        PlayerUpdateInfoAction.ADD_PLAYER -> {
                            val name = buffer.readMcString()
                            val properties = buffer.readPrefixed {
                                GameProfile.Property.decode(buffer)
                            }
                            SinglePlayerAction.AddPlayer(name, properties)
                        }

                        PlayerUpdateInfoAction.INITIALIZE_CHAT -> {
                            buffer.readPrefixOptional {
                                SinglePlayerAction.InitializeChat(
                                    readUuid(), readLong(),
                                    readBytes(512), readBytes(4096)
                                )
                            } ?: SinglePlayerAction.InitializeChat(null, null, null, null)
                        }

                        PlayerUpdateInfoAction.UPDATE_GAME_MODE -> {
                            SinglePlayerAction.UpdateGameMode(buffer.readVarInt())
                        }

                        PlayerUpdateInfoAction.UPDATE_LISTED -> {
                            SinglePlayerAction.UpdateListed(buffer.readBoolean())
                        }

                        PlayerUpdateInfoAction.UPDATE_LATENCY -> {
                            SinglePlayerAction.UpdateLatency(buffer.readVarInt())
                        }

                        PlayerUpdateInfoAction.UPDATE_DISPLAY_NAME -> {
                            val hasDisplayName = buffer.readBoolean()
                            val displayName = if (hasDisplayName) buffer.readTextComponent() else null
                            SinglePlayerAction.UpdateDisplayName(displayName)
                        }

                        PlayerUpdateInfoAction.UPDATE_LIST_PRIORITY -> {
                            SinglePlayerAction.UpdateListPriority(buffer.readVarInt())
                        }

                        PlayerUpdateInfoAction.UPDATE_HAT -> {
                            SinglePlayerAction.UpdateHat(buffer.readBoolean())
                        }
                    }

                    playerActions.add(parsedAction)
                }

                entries.add(PlayerInfoUpdateEntry(playerUuid, playerActions))
            }
            return ClientboundPlayerInfoUpdatePacket(actionsSet, entries)
        }
    }
}