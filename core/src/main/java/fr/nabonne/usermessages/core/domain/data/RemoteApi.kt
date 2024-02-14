package fr.nabonne.usermessages.core.domain.data

import fr.nabonne.usermessages.core.domain.model.Message
import fr.nabonne.usermessages.core.network.model.MessageResponse

interface RemoteApi {
    suspend fun postMessage(message: Message)
    suspend fun fetchAllMessages(): Map<String, List<MessageResponse>>
    suspend fun fetchMessagesForUser(user: String): List<MessageResponse>
}