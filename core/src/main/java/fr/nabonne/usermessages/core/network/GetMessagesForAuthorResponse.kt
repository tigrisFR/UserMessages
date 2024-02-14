package fr.nabonne.usermessages.core.network

import com.google.gson.annotations.SerializedName
import fr.nabonne.usermessages.core.network.model.MessageResponse

data class GetMessagesForAuthorResponse(
    val statusCode: Int,
    val user: String?,
    @SerializedName("message") val messages: List<MessageResponse>?
)
