/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/7
 */


@file:OptIn(DelicateCryptographyApi::class)

package cn.rtast.libmc.protocol.crypto

import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.DelicateCryptographyApi
import dev.whyoleg.cryptography.algorithms.RSA
import dev.whyoleg.cryptography.algorithms.SHA1

internal val provider = CryptographyProvider.Default

public suspend fun rsaEncrypt(publicKeyBytes: ByteArray, data: ByteArray): ByteArray {
    val provider = CryptographyProvider.Default
    val rsa = provider.get(RSA.PKCS1)
    val publicKey = rsa.publicKeyDecoder(SHA1).decodeFromByteArray(RSA.PublicKey.Format.DER, publicKeyBytes)
    return publicKey.encryptor().encrypt(data)
}