package fr.nabonne.usermessages.core.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import fr.nabonne.usermessages.core.network.model.MessageResponse
import java.lang.reflect.Type


class GetMessagesForAuthorResponseDeserializer : JsonDeserializer<GetMessagesForAuthorResponse> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): GetMessagesForAuthorResponse {
        val jsonObject = json.asJsonObject

        val statusCode = jsonObject.get("statusCode").asInt


        val bodyElement = jsonObject.get("body")
        val bodyObject = if (bodyElement.isJsonPrimitive && bodyElement.asJsonPrimitive.isString) {
            val parsedBodyString = JsonParser().parse(bodyElement.asString)
            if (parsedBodyString.isJsonObject) {
                parsedBodyString.asJsonObject
            } else {
                return GetMessagesForAuthorResponse(statusCode, null, null)
            }
        } else {//if (bodyElement.isJsonObject){
            bodyElement.asJsonObject
        }
        var userValue: String? = null
        var messages: List<MessageResponse>? = null
        if (!bodyObject.isJsonNull) {
            for ((key, value) in bodyObject.entrySet()) {

                if (key == "user")
                    userValue = value.asString
                if (key == "message" && value.isJsonArray) {
                    messages = context.deserialize<List<MessageResponse>>(
                        value, object : TypeToken<List<MessageResponse>>() {}.type
                    )
                }
            }
        }
        return GetMessagesForAuthorResponse(statusCode, userValue, messages)
    }
}