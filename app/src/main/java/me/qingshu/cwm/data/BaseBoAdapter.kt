package me.qingshu.cwm.data

import android.R.attr
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type


class BaseBoAdapter:JsonSerializer<Icon>, JsonDeserializer<Icon> {
    override fun serialize(
        src: Icon,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val result = JsonObject()
        result.add("type", JsonPrimitive(src.javaClass.simpleName))
        result.add("properties", context.serialize(src, src.javaClass))

        return result
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Icon {
        val jsonObject = json.asJsonObject
        val type = jsonObject.get("type").asString
        val element = jsonObject.get("properties")

        try {
            // 指定包名+类名
            val thePackage = "me.qingshu.cwm.data."
            return context.deserialize(element, Class.forName(thePackage+type))
        } catch (cnfe:ClassNotFoundException) {
            throw JsonParseException("Unknown element type: $type", cnfe)
        }
    }

}