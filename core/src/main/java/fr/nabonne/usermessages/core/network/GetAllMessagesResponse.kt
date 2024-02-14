package fr.nabonne.usermessages.core.network

import fr.nabonne.usermessages.core.network.model.MessageResponse

data class UserMessagesResponse(
    val statusCode: Int,
    val body: Map<String, List<MessageResponse>>
)