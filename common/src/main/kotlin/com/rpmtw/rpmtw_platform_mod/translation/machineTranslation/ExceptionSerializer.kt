package com.rpmtw.rpmtw_platform_mod.translation.machineTranslation

import com.google.gson.*
import java.lang.reflect.Type

class ExceptionSerializer : JsonSerializer<Exception?> {
    override fun serialize(src: Exception?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.add("cause", JsonPrimitive(src?.cause?.toString()))
        jsonObject.add("message", JsonPrimitive(src?.message))
        return jsonObject
    }
}