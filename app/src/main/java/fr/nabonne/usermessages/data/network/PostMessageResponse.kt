package fr.nabonne.usermessages.data.network

// Response body is as follows:
data class PostMessageResponse(
    val user: String,
    val subject: String,
    val message: String
)