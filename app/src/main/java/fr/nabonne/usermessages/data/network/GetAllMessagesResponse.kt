package fr.nabonne.usermessages.data.network

data class UserMessagesResponse(
    val statusCode: Int,
    val body: Map<String, List<MessageResponse>>
)

data class MessageResponse(
    val subject: String,
    val message: String,
)
