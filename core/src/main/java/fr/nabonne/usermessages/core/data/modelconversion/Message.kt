package fr.nabonne.usermessages.core.data.modelconversion

import fr.nabonne.usermessages.core.domain.model.Message
import fr.nabonne.usermessages.core.network.model.MessageResponse

fun MessageResponse.toMessageModel(user: String): Message {
    return Message(subject = subject, content = message, author = user)
}