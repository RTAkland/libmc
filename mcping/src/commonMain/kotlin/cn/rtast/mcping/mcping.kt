/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

@file:JvmName("McPing")


package cn.rtast.mcping

import cn.rtast.mcping.bedrock.pingBedrockServer
import cn.rtast.mcping.java.pingJavaServer
import cn.rtast.mcping.platform.MCPingContext
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

@JvmOverloads
public fun mcping(
    host: String,
    port: Int,
    type: ServerType = ServerType.Java,
    context: MCPingContext = MCPingContext(),
): PingResponse = when (type) {
    ServerType.Java -> pingJavaServer(host, port, context)
    ServerType.Bedrock -> pingBedrockServer(host, port, context)
}