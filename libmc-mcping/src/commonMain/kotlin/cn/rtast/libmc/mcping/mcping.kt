/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */

@file:JvmName("McPing")


package cn.rtast.libmc.mcping

import cn.rtast.libmc.common.LibMCContext
import cn.rtast.libmc.mcping.bedrock.pingBedrockServer
import cn.rtast.libmc.mcping.java.pingJavaServer
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

@JvmOverloads
public suspend fun mcping(
    host: String,
    port: Int,
    type: ServerType = ServerType.Java,
    context: LibMCContext = LibMCContext(),
): PingResponse = when (type) {
    ServerType.Java -> pingJavaServer(host, port, context)
    ServerType.Bedrock -> pingBedrockServer(host, port, context)
}