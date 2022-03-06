package com.rpmtw.rpmtw_platform_mod.translation.machineTranslation

import com.github.kittinunf.fuel.coroutines.awaitStringResult
import com.github.kittinunf.fuel.httpGet
import com.rpmtw.rpmtw_platform_mod.utilities.Utilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import okhttp3.OkHttpClient
import java.sql.Timestamp
import java.util.*
import java.util.concurrent.ExecutionException


object MachineTranslationManager {
    private val mc = Minecraft.getInstance()
    private val cache: MutableMap<SourceText, MachineTranslationData?> = HashMap()
    private val progress: MutableList<String> = ArrayList()
    private val PROGRESS_TEXT: Component = TextComponent("翻譯中...").withStyle(ChatFormatting.GRAY)
    private val ERROR_TEXT: Component = TextComponent("翻譯失敗").withStyle(ChatFormatting.GRAY)
    private val NO_REQUIRED_TEXT: Component = TextComponent("不需翻譯").withStyle(ChatFormatting.GRAY)
    private val translatedLanguage: String
        get() = when (mc.languageManager.selected.code) {
            "zh_tw" -> "zh_Hant"
            "zh_hk" -> "zh_Hant"
            "zh_cn" -> "zh_Hans"
            else -> "zh_Hant"
        }

    fun createToolTip(source: String): Component {
        val info: MachineTranslationData? = cache[SourceText(source, translatedLanguage)]

        if (info != null) {
            if (info.error != null) return ERROR_TEXT
            if (source == info.text) return NO_REQUIRED_TEXT
            if (info.text != null) {
                return TextComponent("機器翻譯結果: " + info.text).withStyle(ChatFormatting.GRAY)
            }
            return ERROR_TEXT
        }
        if (progress.contains(source)) {
            val c = PROGRESS_TEXT.copy()
            for (i in 0 until System.currentTimeMillis() % 400 / 100) {
                c.append(".")
            }
            return c
        }
        progress.add(source)
        Utilities.coroutineLaunch {
            val data: MachineTranslationData? = try {
                val str: String = translate(source)
                MachineTranslationData(str, null, Timestamp(System.currentTimeMillis()))
            } catch (e: Exception) {
                e.printStackTrace()
                MachineTranslationData(null, e, Timestamp(System.currentTimeMillis()))
            }
            try {
                withContext(Dispatchers.IO) {
                    mc.submit {
                        cache[SourceText(source, translatedLanguage)] = data
                    }.get()
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }
            progress.remove(source)
        }

        return ERROR_TEXT
    }

    private fun randomIP(): String {
        val r = Random()
        return "${r.nextInt(256)}.${r.nextInt(256)}.${r.nextInt(256)}.${r.nextInt(256)}"
    }

    @Suppress("SpellCheckingInspection")
    private suspend fun translate(text: String): String {
        val client = OkHttpClient().newBuilder().build()

        val url =
            "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en_us&tl=${translatedLanguage}&dt=t&q=$text"
        return url.httpGet().header("x-forwarded-for", randomIP()).awaitStringResult().fold({ response ->
            var result: String = response.split("\n".toRegex()).toTypedArray()[0]
            result = result.replace("\"", "").replace("[", "").replace("]", "")
            val filter = result.split(",".toRegex()).toTypedArray()
            result = filter[0]
            client.dispatcher().cancelAll()
            return result
        }, {
            if (it.response.statusCode == 429) {
                "錯誤：連線 Google 翻譯伺服器流量異常"
            } else {
                "錯誤：取得翻譯失敗"
            }
        })
    }
}

data class SourceText(val source: String, val language: String)