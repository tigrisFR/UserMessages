package fr.nabonne.usermessages.core.network

data class PostMessageRequest (
    val user: String,
    val operation: String = "add_message",
    val subject: String,
    val message: String
)