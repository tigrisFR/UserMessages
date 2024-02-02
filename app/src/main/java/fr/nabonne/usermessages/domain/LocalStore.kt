package fr.nabonne.usermessages.domain

import fr.nabonne.usermessages.domain.model.Message
import kotlinx.coroutines.flow.StateFlow

interface LocalStore {
    suspend fun storeAllMessages(
        allMessagesByUser: Map<String, List<Message>>,
        allMessages: List<Message>,
    )
    fun getAllMessagesByUser(): StateFlow<Map<String, List<Message>>>
    fun getAllMessages(): StateFlow<List<Message>>
}