package fr.nabonne.usermessages.core.network

// Response body is as follows:
data class PostMessageResponse(
    val user: String,
    val subject: String,
    val message: String
)