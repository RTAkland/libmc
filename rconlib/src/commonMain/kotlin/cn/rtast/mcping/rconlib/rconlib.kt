/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */

@file:JvmName("Rconlib")

package cn.rtast.mcping.rconlib

import cn.rtast.mcping.platform.MCPingContext
import cn.rtast.mcping.platform._ReadChannel
import cn.rtast.mcping.platform._Socket
import cn.rtast.mcping.platform._WriteChannel
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

public class RCONClient internal constructor(
    private val host: String,
    private val port: Int,
    private val context: MCPingContext,
) : AutoCloseable {
    private var socket: _Socket? = null
    private var readChannel: _ReadChannel? = null
    private var writeChannel: _WriteChannel? = null
    private var currentRequestId = 1

    public fun connect(password: String): Boolean {
        socket = _Socket(host, port, context)
        readChannel = socket!!.openReadChannel()
        writeChannel = socket!!.openWriteChannel()
        val authPacket = AuthPacket(password, currentRequestId)
        writeChannel!!.sendPacket(authPacket)
        return readChannel!!.readPacket().requestId != -1
    }

    public fun command(command: String): String {
        val reqId = currentRequestId++
        val commandPacket = ExecCommandPacket(command, reqId)
        writeChannel!!.sendPacket(commandPacket)
        return readChannel!!.readPacket().payload
    }

    public override fun close() {
        socket?.close()
        socket = null
        readChannel = null
        writeChannel = null
    }
}

@JvmOverloads
public fun rconClient(host: String, port: Int, context: MCPingContext = MCPingContext()): RCONClient =
    RCONClient(host, port, context)