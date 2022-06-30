package com.rpmtw.rpmtw_platform_mod.translation.language

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class TranslateLanguageAdapter : TypeAdapter<TranslateLanguage>() {
    override fun read(`in`: JsonReader): TranslateLanguage {
        return TranslateLanguage.valueOf(`in`.nextString())
    }

    override fun write(out: JsonWriter, language: TranslateLanguage) {
        out.value(language.code)
    }
}