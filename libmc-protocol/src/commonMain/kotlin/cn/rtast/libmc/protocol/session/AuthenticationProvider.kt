/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.protocol.session

public fun interface AuthenticationProvider {
    public suspend fun joinServer(url: String, accessToken: String, uuid: String, serverIdHash: String)
}