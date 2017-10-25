package net.chris.demo.guardian.database.converter

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.raizlabs.android.dbflow.converter.TypeConverter

import java.io.IOException

/**
 * Converts [JsonNode]
 */
@com.raizlabs.android.dbflow.annotation.TypeConverter
class JsonNodeConverter : TypeConverter<String, JsonNode>() {

    var mapper: ObjectMapper = ObjectMapper()

    override fun getDBValue(model: JsonNode?): String? {
        return model?.toString()
    }

    override fun getModelValue(data: String?): JsonNode? {
        try {
            return if (data == null) null else mapper.readTree(data)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }
}
