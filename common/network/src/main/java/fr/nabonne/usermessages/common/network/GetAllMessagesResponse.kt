package fr.nabonne.usermessages.common.network

import fr.nabonne.usermessages.common.domain.data.model.MessageResponse

data class UserMessagesResponse(
    val statusCode: Int,
    val body: Map<String, List<MessageResponse>>
)