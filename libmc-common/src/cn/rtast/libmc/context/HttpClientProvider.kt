/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.context

public fun interface HttpClientProvider {
    public suspend fun post(url: String, body: String, headers: Map<String, String>)
}