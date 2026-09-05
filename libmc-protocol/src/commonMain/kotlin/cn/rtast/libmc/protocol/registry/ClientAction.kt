/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/5
 */


package cn.rtast.libmc.protocol.registry

public enum class ClientAction(public val actionID: Int) {
    /**
     * Sent when the client is ready to respawn after death.
     */
    PerformRespawn(0),

    /**
     * Sent when the client opens the Statistics menu.
     */
    RequestStatus(1),

    /**
     * Sent when the client opens the Edit Game Rules menu.
     */
    RequestGameRuleValues(2);

    public companion object {
        public fun fromID(id: Int): ClientAction = entries.first { it.actionID == id }
    }
}