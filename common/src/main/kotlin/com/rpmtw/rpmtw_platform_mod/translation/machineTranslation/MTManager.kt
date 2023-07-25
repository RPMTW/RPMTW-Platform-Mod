package com.rpmtw.rpmtw_platform_mod.translation.machineTranslation

import com.github.kittinunf.fuel.coroutines.awaitStringResult
import com.github.kittinunf.fuel.httpGet
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.rpmtw.rpmtw_platform_mod.RPMTWPlatformMod
import com.rpmtw.rpmtw_platform_mod.translation.GameLanguage
import com.rpmtw.rpmtw_platform_mod.util.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.minecraft.ChatFormatting
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.TextColor
import org.apache.http.client.utils.URIBuilder
import java.io.File
import java.lang.reflect.Type
import java.sql.Timestamp
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

object MTManager {
    private val cache: MutableMap<String, Map<GameLanguage, MTInfo>> = mutableMapOf()
    private val cacheFile: File = Util.getFileLocation("machine_translation_cache.json")
    private val queue: MutableList<QueueText> = mutableListOf()
    private var handleQueueing: Boolean = false
    private var translatingCount: Int = 0
    private const val MAX_TRANSLATING_COUNT: Int = 3
    private val formatPattern = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)")
    private val gson = GsonBuilder().registerTypeAdapter(Exception::class.java, ExceptionSerializer())
        .registerTypeAdapter(Timestamp::class.java, TimestampAdapter())
        .create()

    private val translatedLanguage: GameLanguage
        get() = GameLanguage.getMinecraft()

    fun create(source: String, vararg i18nArgs: Any? = arrayOf()): MutableComponent {
        val info: MTInfo? = cache[source]?.get(translatedLanguage)

        return if (info?.text != null && info.status == MTDataStatus.SUCCESS) {
            handleI18nComponent(info.text, *i18nArgs).withStyle {
                it.withColor(ChatFormatting.GREEN)
            }
        } else if (info?.status == MTDataStatus.FAILED && info.error != null) {
            Component.literal(I18n.get("machineTranslation.rpmtw_platform_mod.status.failed", info.error)).withStyle(
                ChatFormatting.RED
            )
        } else if (translatingCount >= MAX_TRANSLATING_COUNT) {
            // If there are too many things to translate at the same time, the translation will be skipped to avoid sending too many requests to the server
            generateProgressText()
        } else if (info?.status == MTDataStatus.Translating) {
            generateProgressText()
        } else {
            Util.coroutineLaunch {
                translateAndCache(source)
            }

            generateProgressText()
        }
    }

    fun addToQueue(source: String) {
        val sourceText = QueueText(source, translatedLanguage)
        if (sourceText !in queue) {
            queue.add(sourceText)
        }

        if (!handleQueueing && queue.isNotEmpty() && translatingCount < MAX_TRANSLATING_COUNT) {
            handleQueue()
        }
    }

    fun getFromCache(source: String, vararg i18nArgs: Any? = arrayOf()): MutableComponent? {
        val info: MTInfo? = cache[source]?.get(translatedLanguage)

        if (info?.text == null) return null
        return handleI18nComponent(info.text, *i18nArgs).withStyle {
            // light blue
            it.withColor(TextColor.parseColor("#2f8eed"))
        }
    }

    fun saveCache() {
        if (!cacheFile.exists()) {
            cacheFile.createNewFile()
        }

        gson.toJson(cache).let {
            cacheFile.writeText(it)
        }
        RPMTWPlatformMod.LOGGER.info("Machine translation cache saved.")
    }

    fun readCache() {
        try {
            if (cacheFile.exists()) {
                val type: Type = object : TypeToken<MutableMap<String, Map<GameLanguage, MTInfo>>>() {}.type
                gson.fromJson<MutableMap<String, Map<GameLanguage, MTInfo>>>(cacheFile.reader(), type).let {
                    cache.putAll(it)
                }
                RPMTWPlatformMod.LOGGER.info("Successful read machine translation cache")
            }
        } catch (e: Exception) {
            RPMTWPlatformMod.LOGGER.error("Failed to read machine translation cache file", e)
            cacheFile.delete()
        }
    }


    private fun handleI18nComponent(text: String, vararg args: Any? = arrayOf()): MutableComponent {
        fun getArgument(i: Int): Component {
            return when (val obj = args.getOrNull(i)) {
                null -> {
                    Component.empty()
                }

                is Component -> {
                    obj
                }

                else -> {
                    Component.literal(obj.toString())
                }
            }
        }

        val component: MutableComponent = Component.empty()

        val matcher = formatPattern.matcher(text)
        try {
            var i = 0
            var j: Int
            var l: Int
            j = 0
            while (matcher.find(j)) {
                val k = matcher.start()
                l = matcher.end()
                var string2: String
                if (k > j) {
                    string2 = text.substring(j, k)
                    require(string2.indexOf(37.toChar()) == -1)
                    component.append(string2)
                }
                string2 = matcher.group(2)
                val string3 = text.substring(k, l)
                if ("%" == string2 && "%%" == string3) {
                    component.append("%")
                } else {
                    if ("s" == string2) {
                        val string4 = matcher.group(1)
                        val m = if (string4 != null) string4.toInt() - 1 else i++
                        if (m < args.size) {
                            val argument = getArgument(m)
                            component.append(argument)
                        }
                    }
                }
                j = l
            }
            if (j < text.length) {
                val string5 = text.substring(j)
                require(string5.indexOf(37.toChar()) == -1)
                component.append(string5)
            }
        } catch (_: IllegalArgumentException) {
        }

        return component
    }

    private fun handleQueue() {
        handleQueueing = true
        Util.coroutineLaunch {
            while (queue.isNotEmpty() && translatingCount < MAX_TRANSLATING_COUNT) {
                val sourceText = queue.removeAt(0)
                translateAndCache(sourceText.text)
                withContext(Dispatchers.IO) {
                    // Sleep for 2 second to prevent spamming the Google Translate API
                    Thread.sleep(TimeUnit.SECONDS.toMillis(2))
                }
            }
            handleQueueing = false
        }
    }

    private fun generateProgressText(): MutableComponent {
        val text = Component.translatable("machineTranslation.rpmtw_platform_mod.status.translating")
            .withStyle(ChatFormatting.GRAY)
        for (i in 0 until System.currentTimeMillis() % 400 / 100) {
            text.append(".")
        }
        return text
    }

    private suspend fun translateAndCache(source: String) {
        translatingCount++
        if (cache[source] == null) {
            cache[source] = mapOf()
        }
        try {
            val translatingData = MTInfo(
                timestamp = Timestamp(System.currentTimeMillis()), status = MTDataStatus.Translating
            )

            cache[source] = cache[source]!!.plus(translatedLanguage to translatingData)

            val result: String = translate(source)
            val translatedData = MTInfo(
                text = result, timestamp = Timestamp(System.currentTimeMillis()), status = MTDataStatus.SUCCESS
            )
            cache[source] = cache[source]!!.plus(translatedLanguage to translatedData)

        } catch (e: Exception) {
            RPMTWPlatformMod.LOGGER.error("Translation failed", e)
            val failedData = MTInfo(
                timestamp = Timestamp(System.currentTimeMillis()), error = e, status = MTDataStatus.FAILED
            )
            cache[source] = cache[source]!!.plus(translatedLanguage to failedData)
        } finally {
            translatingCount--
        }
    }

    @Suppress("SpellCheckingInspection")
    private suspend fun translate(text: String): String {
        val builder = URIBuilder("https://translate.googleapis.com/")
        builder.path = "translate_a/single"
        builder.addParameter("client", "gtx")
        builder.addParameter("sl", "en")
        builder.addParameter("tl", translatedLanguage.code)
        builder.addParameter("dt", "t")
        builder.addParameter("q", text)
        builder.addParameter("format", "json")
        val url: String = builder.build().toString()

        return url.httpGet().awaitStringResult().fold({ response ->
            return@fold JsonParser.parseString(response).asJsonArray[0].asJsonArray[0].asJsonArray[0].asString
        }, {
            if (it.response.statusCode == 429) {
                throw Exception(I18n.get("machineTranslation.rpmtw_platform_mod.status.rate_limit"))
            } else {
                throw Exception(
                    I18n.get(
                        "machineTranslation.rpmtw_platform_mod.status.exception", it.response.statusCode
                    )
                )
            }
        })
    }
}