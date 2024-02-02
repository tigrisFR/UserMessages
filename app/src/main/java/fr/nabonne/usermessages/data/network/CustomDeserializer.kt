package fr.nabonne.usermessages.data.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import fr.nabonne.usermessages.domain.MessageResponse
import java.lang.reflect.Type

class UserMessagesResponseDeserializer : JsonDeserializer<UserMessagesResponse> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): UserMessagesResponse {
        val jsonObject = json.asJsonObject

        val statusCode = jsonObject.get("statusCode").asInt

        val body = mutableMapOf<String, List<MessageResponse>>()
        val bodyElement = jsonObject.get("body")
        val bodyObject = if (bodyElement.isJsonPrimitive && bodyElement.asJsonPrimitive.isString) {
            JsonParser().parse(bodyElement.asString).asJsonObject
        } else {//if (bodyElement.isJsonObject){
            bodyElement.asJsonObject
        }
        if (!bodyObject.isJsonNull) {
            for ((key, value) in bodyObject.entrySet()) {
                if (value.isJsonArray) {
                    val messages = context.deserialize<List<MessageResponse>>(
                        value, object : TypeToken<List<MessageResponse>>() {}.type
                    )
                    body[key] = messages
                }
            }
        }
        return UserMessagesResponse(statusCode, body)
    }
}