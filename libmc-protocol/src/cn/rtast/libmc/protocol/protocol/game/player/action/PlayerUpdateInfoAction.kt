/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.protocol.protocol.game.player.action

import cn.rtast.libmc.protocol.protocol.game.chat.TextComponent
import cn.rtast.libmc.protocol.protocol.game.session.GameProfile
import kotlin.uuid.Uuid

public enum class PlayerUpdateInfoAction(public val mask: Int) {
    ADD_PLAYER(0x01),
    INITIALIZE_CHAT(0x02),
    UPDATE_GAME_MODE(0x04),
    UPDATE_LISTED(0x08),
    UPDATE_LATENCY(0x10),
    UPDATE_DISPLAY_NAME(0x20),
    UPDATE_LIST_PRIORITY(0x40),
    UPDATE_HAT(0x80);

    public companion object {
        public fun parseActions(mask: Int): Set<PlayerUpdateInfoAction> {
            val set = HashSet<PlayerUpdateInfoAction>()
            for (action in entries) if ((mask and action.mask) != 0) set.add(action)
            return set
        }

        public fun toMask(actions: Set<PlayerUpdateInfoAction>): Int {
            var mask = 0
            for (action in actions) mask = mask or action.mask
            return mask
        }
    }
}

public sealed class SinglePlayerAction {
    public data class AddPlayer(val name: String, val properties: List<GameProfile.Property>) : SinglePlayerAction()

    public data class InitializeChat(
        val sessionId: Uuid?,
        val keyExpiryTime: Long?,
        val encodedPublicKey: ByteArray?,
        val publicKeySignature: ByteArray?,
    ) : SinglePlayerAction() {
        val hasSignatureData: Boolean get() = sessionId != null
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false
            other as InitializeChat
            if (keyExpiryTime != other.keyExpiryTime) return false
            if (sessionId != other.sessionId) return false
            if (!encodedPublicKey.contentEquals(other.encodedPublicKey)) return false
            if (!publicKeySignature.contentEquals(other.publicKeySignature)) return false
            if (hasSignatureData != other.hasSignatureData) return false
            return true
        }

        override fun hashCode(): Int {
            var result = keyExpiryTime.hashCode()
            result = 31 * result + sessionId.hashCode()
            result = 31 * result + (encodedPublicKey?.contentHashCode() ?: 0)
            result = 31 * result + (publicKeySignature?.contentHashCode() ?: 0)
            result = 31 * result + hasSignatureData.hashCode()
            return result
        }
    }

    public data class UpdateGameMode(val gameMode: Int) : SinglePlayerAction()
    public data class UpdateListed(val listed: Boolean) : SinglePlayerAction()
    public data class UpdateLatency(val ping: Int) : SinglePlayerAction()
    public data class UpdateDisplayName(val displayName: TextComponent?) : SinglePlayerAction()
    public data class UpdateListPriority(val priority: Int) : SinglePlayerAction()
    public data class UpdateHat(val visible: Boolean) : SinglePlayerAction()
}

public data class PlayerInfoUpdateEntry(val uuid: Uuid, val actions: List<SinglePlayerAction>)