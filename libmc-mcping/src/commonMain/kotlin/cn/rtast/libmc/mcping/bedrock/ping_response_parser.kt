/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.libmc.mcping.bedrock

import cn.rtast.libmc.mcping.PingResponse

public enum class BedrockGameMode(public val gameMode: String) {
    Survival("Survival"),
    Creative("Creative"),
    Adventure("Adventure"),
    Spectator("Spectator"), // reserved?
    Hardcore("Hardcore"),
    Unknown("");  // reserved

    public companion object {
        public fun parse(value: String?): BedrockGameMode {
            return entries.firstOrNull {
                it.name.equals(value, ignoreCase = true)
            } ?: Unknown
        }
    }
}

public data class BedrockPingResponse(
    /**
     * always be `MCPE`
     * index 0
     */
    val protocolHeader: String,
    /**
     * MOTD line 1
     * index 1
     */
    val motdLine1: String,
    /**
     * protocol version
     * index 2
     */
    val protocolVersion: Int,
    /**
     * game version
     * index 3
     */
    val gameVersion: String,
    /**
     * online players
     * index 4
     */
    val onlinePlayers: Int,
    /**
     * maximum players
     * index 5
     */
    val maximumPlayers: Int,
    /**
     * server GUID
     * parsed as String -> origin bytes count is 8
     * index 6
     */
    val serverGUID: String,
    /**
     * MOTD line 2
     * index 7
     */
    val motdLine2: String,
    /**
     * game mode
     * index 8
     */
    val gameMode: BedrockGameMode,
    /**
     * Nintendo Limited
     * Nintendo Switch online restriction flag (1 indicates restriction enabled/processed)
     * index 9
     */
    val nintendoLimited: Boolean,
    /**
     * ipv4 port
     * if disabled or unconfigured, it will be 0
     * index 10
     */
    val ipv4Port: Int,
    /**
     * ipv6 port
     * if disabled or unconfigured, it will be 0
     * index 11
     */
    val ipv6Port: Int,
    /**
     * latency ms
     * reserved not exists in response packet
     */
    val latency: Int
)

internal fun PingResponse.parseBedrockPingResponse(): BedrockPingResponse {
    try {
        val fields = content.split(";")
        return BedrockPingResponse(
            fields[0], fields[1], fields[2].toInt(),
            fields[3], fields[4].toInt(),
            fields[5].toInt(), fields[6],
            fields[7], BedrockGameMode.parse(fields[8]),
            fields.getOrNull(9) == "1", fields[10].toInt(),
            fields[11].toInt(), this.latency  // pass through PingResponse.latency
        )
    } catch (e: Exception) {
        e.printStackTrace()
        throw IllegalStateException("The server respond incorrect response: Missing fields or type mismatch")
    }
}