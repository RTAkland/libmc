/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */

package cn.rtast.libmc.protocol.protocol

import cn.rtast.libmc.protocol.packet.configuration.clientbound.*
import cn.rtast.libmc.protocol.packet.configuration.serverbound.*
import cn.rtast.libmc.protocol.packet.handshake.ServerboundHandshakePacket
import cn.rtast.libmc.protocol.packet.login.clientbound.ClientboundDisconnectLoginPacket
import cn.rtast.libmc.protocol.packet.login.clientbound.ClientboundLoginSuccessPacket
import cn.rtast.libmc.protocol.packet.login.clientbound.ClientboundSetCompressionPacket
import cn.rtast.libmc.protocol.packet.login.serverbound.ServerboundLoginAcknowledgedPacket
import cn.rtast.libmc.protocol.packet.login.serverbound.ServerboundLoginStartPacket
import cn.rtast.libmc.protocol.packet.play.clientbound.*
import cn.rtast.libmc.protocol.packet.play.serverbound.*
import cn.rtast.libmc.protocol.packet.status.serverbound.ServerboundPingRequest
import cn.rtast.libmc.protocol.protocol.state.ProtocolState
import cn.rtast.libmc.protocol.protocol.state.ProtocolStateRegistry

internal object GameProtocols {
    val clientboundGameProtocols = ProtocolStateRegistry().apply {
        register(ProtocolState.CONFIGURATION) {
            register(0x00, ClientboundCookieRequestPacket)
            register(0x01, ClientboundCustomPayloadPacket)
            register(0x02, ClientboundDisconnectConfigurationPacket)
            register(0x03, ClientboundFinishConfigurationPacket)
            register(0x04, ClientboundKeepAliveConfigurationPacket)
            register(0x05, ClientboundPingConfigurationPacket)
            register(0x0e, ClientboundSelectKnownPacksPacket)
        }
        register(ProtocolState.LOGIN) {
            register(0x00, ClientboundDisconnectLoginPacket)
            register(0x02, ClientboundLoginSuccessPacket)
            register(0x03, ClientboundSetCompressionPacket)
        }
        register(ProtocolState.PLAY) {
            register(0x00, ClientboundDelimiterPacket)
            register(0x01, ClientboundSpawnEntityPacket)
            register(0x02, ClientboundEntityAnimationPacket)
            register(0x03, ClientboundAwardStatisticsPacket)
            register(0x04, ClientboundAcknowledgeBlockChangePacket)
            register(0x05, ClientboundBlockDestructionPacket)
            register(0x06, ClientboundBlockEntityDataPacket)

            register(0x20, ClientboundDisconnectPlayPacket)
            register(0x2c, ClientboundKeepAlivePlayPacket)
            register(0x31, ClientboundLoginPlayPacket)
            register(0x3d, ClientboundPingPlayPacket)
            register(0x41, ClientboundPlayerChatMessagePacket)
            register(0x76, ClientboundStartConfigurationPacket)
            register(0x79, ClientboundSystemChatMessagePacket)
        }
    }

    val serverboundGameProtocols = ProtocolStateRegistry().apply {
        register(ProtocolState.HANDSHAKE) {
            register(0x00, ServerboundHandshakePacket)
        }
        register(ProtocolState.CONFIGURATION) {
            register(0x00, ServerboundCookieResponsePacket)
            register(0x02, ServerboundCustomPayloadPacket)
            register(0x03, ServerboundAckFinishConfigurationPacket)
            register(0x04, ServerboundKeepAliveConfigurationPacket)
            register(0x05, ServerboundPongConfigurationPacket)
            register(0x07, ServerboundSelectKnownPacksPacket)
        }
        register(ProtocolState.LOGIN) {
            register(0x00, ServerboundLoginStartPacket)
            register(0x03, ServerboundLoginAcknowledgedPacket)
        }
        register(ProtocolState.PLAY) {
            register(0x00, ServerboundAcceptTeleportationPacket)
            register(0x01, ServerboundAttackActionPacket)
            register(0x02, ServerboundQueryBlockEntityTagPacket)
            register(0x03, ServerboundBundleItemSelectedPacket)
            register(0x04, ServerboundChangeDifficultyPacket)
            register(0x05, ServerboundChangeGameModePacket)
            register(0x06, ServerboundAcknowledgeChatMessagePacket)
            register(0x07, ServerboundChatCommandPacket)
            register(0x08, ServerboundSignedChatCommandPacket)
            register(0x09, ServerboundChatMessagePacket)
            register(0x0a, ServerboundUpdateChatSessionPacket)
            register(0x0b, ServerboundChunkBatchReceivedPacket)
            register(0x0c, ServerboundClientCommandPacket)
            register(0x0d, ServerboundClientTickEndPacket)
            register(0x0e, ServerboundClientInformationPacket)
            register(0x0f, ServerboundCommandSuggestionRequestPacket)
            register(0x10, ServerboundConfigurationAcknowledgedPacket)
            register(0x11, ServerboundContainerClickButtonPacket)
            register(0x12, ServerboundContainerClickPacket)
            register(0x13, ServerboundContainerClosePacket)
            register(0x14, ServerboundChangeContainerSlotStatePacket)
            register(0x15, ServerboundCookieResponsePacket)
            register(0x16, ServerboundCustomPayloadPacket)
            register(0x17, ServerboundDebugSubscriptionRequestPacket)
            register(0x18, ServerboundEditBookPacket)
            register(0x19, ServerboundQueryEntityTagPacket)
            register(0x1a, ServerboundInteractPacket)
            register(0x1b, ServerboundJigsawGeneratePacket)
            register(0x1c, ServerboundKeepAlivePlayPacket)
            register(0x1d, ServerboundLockDifficultyPacket)
            register(0x1e, ServerboundSetPlayerPositionPacket)
            register(0x1f, ServerboundSetPlayerPositionAndRotationPacket)
            register(0x20, ServerboundSetPlayerRotationPacket)
            register(0x21, ServerboundSetPlayerMovementFlagPacket)
            register(0x22, ServerboundMoveVehiclePacket)
            register(0x23, ServerboundPaddleBoatPacket)
            register(0x24, ServerboundPickItemFromBlockPacket)
            register(0x25, ServerboundPickItemFromEntityPacket)
            register(0x26, ServerboundPingRequest)
            register(0x27, ServerboundPlaceRecipePacket)
            register(0x28, ServerboundPlayerAbilitiesPacket)
            register(0x29, ServerboundPlayerActionPacket)
            register(0x2a, ServerboundPlayerCommandPacket)
            register(0x2b, ServerboundPlayerInputPacket)
            register(0x2c, ServerboundPlayerLoadedPacket)
            register(0x2d, ServerboundPongPlayPacket)
            register(0x2e, ServerboundRecipeBookChangeSettingsPacket)
            register(0x2f, ServerboundRecipeBookSeenRecipePacket)
            register(0x30, ServerboundRenameItemPacket)
            register(0x31, ServerboundResourcePackResponsePacket)
            register(0x32, ServerboundSeenAdvancementsPacket)
            register(0x33, ServerboundSelectTradePacket)
            register(0x34, ServerboundSetBeaconPacket)
            register(0x35, ServerboundSetCarriedItemPacket)
            register(0x36, ServerboundSetCommandBlockPacket)
            register(0x37, ServerboundSetCommandMinecartPacket)
            register(0x38, ServerboundSetCreativeModeSlotPacket)
            register(0x39, ServerboundSetGameRulePacket)
            register(0x3a, ServerboundSetJigsawBlockPacket)
            register(0x3b, ServerboundSetStructureBlockPacket)
            register(0x3c, ServerboundSetTestBlockPacket)
            register(0x3d, ServerboundSignUpdatePacket)
            register(0x3e, ServerboundSpectatorActionPacket)
            register(0x3f, ServerboundSwingPacket)
            register(0x40, ServerboundTeleportToEntityPacket)
            register(0x41, ServerboundTestInstanceBlockActionPacket)
            register(0x42, ServerboundUseItemOnPacket)
            register(0x43, ServerboundUseItemPacket)
            register(0x44, ServerboundCustomClickActionPacket)
        }
    }
}