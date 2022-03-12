package com.rpmtw.rpmtw_platform_mod.translation.machineTranslation

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.apache.commons.lang3.LocaleUtils
import java.util.*

class LocaleAdapter : TypeAdapter<Locale>() {
    override fun read(`in`: JsonReader): Locale {
        return LocaleUtils.toLocale(`in`.nextString())
    }

    override fun write(out: JsonWriter, locale: Locale) {
        out.value(locale.toString())
    }
}