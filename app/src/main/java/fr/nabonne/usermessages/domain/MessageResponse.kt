package fr.nabonne.usermessages.domain

import fr.nabonne.usermessages.domain.model.Message

data class MessageResponse(
    val subject: String,
    val message: String,
) {
    fun toMessageModel(user: String): Message {
        return Message(subject = subject, content = message, author = user)
    }
}