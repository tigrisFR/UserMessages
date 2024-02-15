package fr.nabonne.usermessages.common.domain.data

import fr.nabonne.usermessages.common.domain.model.Message
import kotlinx.coroutines.flow.StateFlow

interface LocalStore {
    suspend fun storeAllMessages(
        allMessagesByUser: Map<String, List<Message>>,
        allMessages: List<Message>,
    )
    fun getAllMessagesByUser(): StateFlow<Map<String, List<Message>>>
    fun getAllMessages(): StateFlow<List<Message>>
    suspend fun storeMessagesForAuthor(author: String, messages: List<Message>)
    fun getMessagesForAuthor(author: String): StateFlow<Pair<String, List<Message>>>
}