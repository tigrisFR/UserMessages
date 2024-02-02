package fr.nabonne.usermessages.data.network

import com.google.gson.annotations.SerializedName
import fr.nabonne.usermessages.domain.MessageResponse

data class GetMessagesForAuthorResponse(
    val statusCode: Int,
    val user: String?,
    @SerializedName("message") val messages: List<MessageResponse>?
)
