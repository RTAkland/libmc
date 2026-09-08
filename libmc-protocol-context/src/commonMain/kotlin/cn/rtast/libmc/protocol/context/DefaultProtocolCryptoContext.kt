/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.protocol.context

import cn.rtast.libmc.crypto.AuthenticationProvider
import cn.rtast.libmc.crypto.ProtocolContextBuilder
import cn.rtast.libmc.crypto.RSA1024Encryptor
import cn.rtast.libmc.crypto.Sha1Hasher
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

private val httpClient: HttpClient = HttpClient()

public val DefaultProtocolContext: ProtocolContextBuilder.() -> Unit = {
    rsaEncryptor = RSA1024Encryptor { key, data -> rsaEncrypt(key, data) }
    sha1Hasher = Sha1Hasher { serverId, secretKey, publicKey -> minecraftServerIdHash(serverId, secretKey, publicKey) }
    cipherFactory = { key -> AesCFB8Cipher(key) }
    authProvider = AuthenticationProvider { url, accessToken, uuid, serverIdHash ->
        val status = httpClient.post(url) {
            headers { header("Content-Type", "application/json") }
            setBody("{\"accessToken\":\"$accessToken\", \"selectedProfile\":\"$uuid\", \"serverId\":\"$serverIdHash\"}")
        }.status
        require(status == HttpStatusCode.NoContent)
    }
}