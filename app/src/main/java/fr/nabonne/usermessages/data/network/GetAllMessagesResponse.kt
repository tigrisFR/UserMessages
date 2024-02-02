package fr.nabonne.usermessages.data.network

import fr.nabonne.usermessages.domain.MessageResponse

data class UserMessagesResponse(
    val statusCode: Int,
    val body: Map<String, List<MessageResponse>>
)