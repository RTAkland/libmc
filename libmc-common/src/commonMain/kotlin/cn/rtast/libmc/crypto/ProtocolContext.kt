/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.crypto

public data class ProtocolContext(
    val authProvider: AuthenticationProvider?,
)

public class ProtocolContextBuilder(private val onlineMode: Boolean) {
    public lateinit var authProvider: AuthenticationProvider

    public fun build(): ProtocolContext =
        ProtocolContext(
            authProvider = if (onlineMode) {
                if (::authProvider.isInitialized) authProvider else error("authProvider is required in online mode")
            } else if (::authProvider.isInitialized) authProvider else null,
        )
}