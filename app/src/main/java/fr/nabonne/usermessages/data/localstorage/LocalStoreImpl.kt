package fr.nabonne.usermessages.data.localstorage

import fr.nabonne.usermessages.domain.LocalStore
import fr.nabonne.usermessages.domain.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalStoreImpl() : LocalStore {
    private val _cachedAllMessages = MutableStateFlow<List<Message>>(emptyList())
    private val _cachedAllMessagesByUser = MutableStateFlow<Map<String, List<Message>>>(emptyMap())
    override suspend fun storeAllMessages(
        allMessagesByUser: Map<String, List<Message>>,
        allmessages: List<Message>,
    ) {
        _cachedAllMessages.value = allmessages
        _cachedAllMessagesByUser.value = allMessagesByUser
    }

    override fun getAllMessagesByUser(): StateFlow<Map<String, List<Message>>> {
        return _cachedAllMessagesByUser.asStateFlow()
    }

    override fun getAllMessages(): StateFlow<List<Message>> {
        return _cachedAllMessages.asStateFlow()
    }

    override suspend fun storeMessagesForAuthor(author: String, messages: List<Message>) {
        //TODO: Implement
        throw NotImplementedError()
    }

    override fun getMessagesForAuthor(author: String): StateFlow<Pair<String, List<Message>>> {
        //TODO: Implement
        throw NotImplementedError()
    }

}