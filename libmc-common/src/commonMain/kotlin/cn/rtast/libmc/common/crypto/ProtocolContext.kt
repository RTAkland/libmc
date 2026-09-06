/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.common.crypto

public data class ProtocolContext(
    val rsaEncryptor: RSA1024Encryptor,
    val sha1Hasher: Sha1Hasher,
    val cipherFactory: (sharedKey: ByteArray) -> NetworkCipher,
    val authProvider: AuthenticationProvider,
)

public class ProtocolContextBuilder(private val onlineMode: Boolean) {
    public lateinit var rsaEncryptor: RSA1024Encryptor
    public lateinit var sha1Hasher: Sha1Hasher
    public lateinit var cipherFactory: (sharedKey: ByteArray) -> NetworkCipher
    public lateinit var authProvider: AuthenticationProvider

    public fun build(): ProtocolContext = ProtocolContext(
        rsaEncryptor = if (::rsaEncryptor.isInitialized) rsaEncryptor else error("rsaEncryptor is required"),
        sha1Hasher = if (::sha1Hasher.isInitialized) sha1Hasher else error("sha1Hasher is required"),
        cipherFactory = if (::cipherFactory.isInitialized) cipherFactory else error("cipherFactory is required"),
        authProvider = if (::authProvider.isInitialized) if (onlineMode) authProvider
        else error("authProvider is required") else error("authProvider is required")
    )
}

public fun interface RSA1024Encryptor {
    public suspend fun encrypt(key: ByteArray, data: ByteArray): ByteArray
}

public fun interface Sha1Hasher {
    public suspend fun hash(serverId: String, secretKey: ByteArray, publicKey: ByteArray): String
}