package fr.nabonne.usermessages.domain

import fr.nabonne.usermessages.domain.model.Message

interface RemoteApi {
    suspend fun postMessage(message: Message)
    suspend fun fetchAllMessages(): Map<String, List<MessageResponse>>
    suspend fun fetchMessagesForUser(user: String): List<Message>
}