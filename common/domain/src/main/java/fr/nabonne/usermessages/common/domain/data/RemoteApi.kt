package fr.nabonne.usermessages.common.domain.data

import fr.nabonne.usermessages.common.domain.data.model.MessageResponse
import fr.nabonne.usermessages.common.domain.model.Message

interface RemoteApi {
    suspend fun postMessage(message: Message)
    suspend fun fetchAllMessages(): Map<String, List<MessageResponse>>
    suspend fun fetchMessagesForUser(user: String): List<MessageResponse>
}