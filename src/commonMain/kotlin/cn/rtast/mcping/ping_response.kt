/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package cn.rtast.mcping

import cn.rtast.mcping.bedrock.BedrockPingResponse
import cn.rtast.mcping.bedrock.parseBedrockPingResponse

public data class PingResponse(
    /**
     * raw response content
     */
    public val content: String,
    /**
     * latency ms
     */
    public val latency: Int,
) {
    public fun toBedrockResponse(): BedrockPingResponse = parseBedrockPingResponse()
}