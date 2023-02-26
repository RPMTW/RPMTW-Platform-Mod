package com.rpmtw.rpmtw_platform_mod.handlers

import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.config.RPMTWConfig
import com.rpmtw.rpmtw_platform_mod.util.Util
import com.sun.net.httpserver.HttpServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.minecraft.client.resources.language.I18n
import org.apache.http.NameValuePair
import org.apache.http.client.utils.URIBuilder
import org.apache.http.client.utils.URLEncodedUtils
import java.io.OutputStream
import java.net.BindException
import java.net.InetSocketAddress
import java.nio.charset.StandardCharsets
import java.util.concurrent.CountDownLatch
import java.util.stream.Collectors


object RPMTWAuthHandler {
    private const val port: Int = 37953
    private var tryCount: Int = 0

    fun login(port: Int = this.port) {
        if (tryCount > 5) {
            RPMTWPlatformMod.LOGGER.error("Failed to bind to port $port")
            return
        }

        Util.coroutineLaunch {
            var server: HttpServer? = null
            try {
                server = HttpServer.create(InetSocketAddress(port), 0)
                val latch = CountDownLatch(1)

                server.createContext("/callback") { handler ->
                    val query: Map<String, String> =
                        URLEncodedUtils.parse(handler.requestURI, StandardCharsets.UTF_8).stream()
                            .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue))

                    val token: String? = query["access_token"]
                    handler.responseHeaders.add("Content-Type", "text/plain; charset=utf-8")

                    if (token != null) {
                        RPMTWConfig.get().rpmtwAuthToken = token
                        RPMTWConfig.save()
                        UniverseChatHandler.restart()

                        val message = I18n.get("auth.rpmtw_platform_mod.status.success")
                        handler.sendResponseHeaders(200, message.toByteArray(Charsets.UTF_8).size.toLong())
                        val res: OutputStream = handler.responseBody
                        res.writer(Charsets.UTF_8).use { it.write(message) }
                        res.close()

                        tryCount = 0
                        latch.countDown()
                    } else {
                        RPMTWPlatformMod.LOGGER.warn("Failed to get rpmtw auth token from callback")

                        val message = I18n.get("auth.rpmtw_platform_mod.status.failed")
                        handler.sendResponseHeaders(200, message.toByteArray(Charsets.UTF_8).size.toLong())
                        val res: OutputStream = handler.responseBody
                        res.writer(Charsets.UTF_8).use { it.write(message) }
                        res.close()
                    }
                }

                // Start http server
                RPMTWPlatformMod.LOGGER.info("Begin listening on http://localhost:$port/callback for successful rpmtw login")
                server.start()
                val uri = URIBuilder("https://account.rpmtw.com")
                    .setParameter("redirect_uri", "http://localhost:$port/callback")
                    .setParameter("service_name", "RPMTW Platform Mod").build()

                Util.openLink(uri.toString())

                withContext(Dispatchers.IO) {
                    // Wait for login
                    latch.await()
                }
            } catch (e: BindException) {
                tryCount++
                // Try binding to another port
                login(port + 1)
            } finally {
                server?.stop(2)
            }
        }
    }

    fun logout() {
        if (!RPMTWConfig.get().isLogin()) return
        RPMTWConfig.get().rpmtwAuthToken = null
        RPMTWConfig.save()
        UniverseChatHandler.restart()
    }
}