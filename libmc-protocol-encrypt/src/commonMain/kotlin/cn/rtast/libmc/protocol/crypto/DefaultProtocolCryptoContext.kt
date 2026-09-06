/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


package cn.rtast.libmc.protocol.crypto

import cn.rtast.libmc.common.crypto.AuthenticationProvider
import cn.rtast.libmc.common.crypto.ProtocolContextBuilder
import cn.rtast.libmc.common.crypto.RSA1024Encryptor
import cn.rtast.libmc.common.crypto.Sha1Hasher
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

private val httpClient = HttpClient()

public val DefaultProtocolCryptoContext: ProtocolContextBuilder.() -> Unit = {
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