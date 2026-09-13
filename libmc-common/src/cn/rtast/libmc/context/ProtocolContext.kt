/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.context

public data class ProtocolContext(
    val httpClient: HttpClientProvider?,
)

public class ProtocolContextBuilder(private val onlineMode: Boolean) {
    public lateinit var httpClientProvider: HttpClientProvider

    public fun build(): ProtocolContext =
        ProtocolContext(
            httpClient = if (onlineMode) {
                if (::httpClientProvider.isInitialized) httpClientProvider else error("Http Client is required in online mode")
            } else if (::httpClientProvider.isInitialized) httpClientProvider else null,
        )
}