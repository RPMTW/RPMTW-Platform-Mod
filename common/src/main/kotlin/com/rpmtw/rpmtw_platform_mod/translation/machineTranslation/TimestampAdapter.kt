package com.rpmtw.rpmtw_platform_mod.translation.machineTranslation

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.sql.Timestamp

class TimestampAdapter : TypeAdapter<Timestamp>() {
    @Throws(IOException::class)
    override fun read(reader: JsonReader): Timestamp {
        return Timestamp(reader.nextLong())
    }

    @Throws(IOException::class)
    override fun write(writer: JsonWriter, timestamp: Timestamp) {
        writer.value(timestamp.time)
    }
}