package fr.nabonne.usermessages.common.domain.data.model

import fr.nabonne.usermessages.common.domain.model.Message

data class MessageResponse(
    val subject: String,
    val message: String,
) {
    fun toMessageModel(user: String): Message {
        return Message(subject = subject, content = message, author = user)
    }
}