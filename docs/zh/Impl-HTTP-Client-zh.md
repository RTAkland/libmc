# 前言

`libmc-protocol-context` 中提供了默认HTTP客户端实现, 如果使用了此模块则不需要手动配置HTTP客户端认证部分

# 基于Ktor的HTTP客户端

```kotlin
private val httpClient = HttpClient()

public val CustomProtocolContext: ProtocolContextBuilder.() -> Unit = {
    authProvider = AuthenticationProvider { url, accessToken, uuid, serverIdHash ->
        val status = httpClient.post(url) {
            headers { header("Content-Type", "application/json") }
            setBody("{\"accessToken\":\"$accessToken\", \"selectedProfile\":\"$uuid\", \"serverId\":\"$serverIdHash\"}")
        }.status
        require(status == HttpStatusCode.NoContent)
    }
}
```

# 基于Java8 HttpURLConnection的HTTP客户端

```kotlin
private fun sendJoinRequest(url: String, accessToken: String, uuid: String, serverIdHash: String): Boolean {
    val connection = (URL(url).openConnection() as HttpURLConnection).apply {
        requestMethod = "POST"
        setRequestProperty("Content-Type", "application/json")
        doOutput = true
        connectTimeout = 5000
        readTimeout = 5000
    }
    val jsonPayload = """{"accessToken":"$accessToken","selectedProfile":"$uuid","serverId":"$serverIdHash"}"""
    connection.outputStream.use { os -> os.write(jsonPayload.toByteArray(Charsets.UTF_8)) }
    val responseCode = connection.responseCode
    connection.disconnect()
    return responseCode == HttpURLConnection.HTTP_NO_CONTENT
}

public val CustomProtocolContext: ProtocolContextBuilder.() -> Unit = {
    authProvider = AuthenticationProvider { url, accessToken, uuid, serverIdHash ->
        val success = sendJoinRequest(url, accessToken, uuid, serverIdHash)
        require(success) { "Failed to authenticate with Mojang session server" }
    }
}
```