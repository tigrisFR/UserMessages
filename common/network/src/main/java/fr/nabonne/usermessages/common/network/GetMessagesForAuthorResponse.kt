package fr.nabonne.usermessages.common.network

import com.google.gson.annotations.SerializedName
import fr.nabonne.usermessages.common.domain.data.model.MessageResponse

data class GetMessagesForAuthorResponse(
    val statusCode: Int,
    val user: String?,
    @SerializedName("message") val messages: List<MessageResponse>?
)
