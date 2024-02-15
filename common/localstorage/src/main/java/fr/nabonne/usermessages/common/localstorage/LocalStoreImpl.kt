package fr.nabonne.usermessages.common.localstorage

import fr.nabonne.usermessages.common.domain.data.LocalStore
import fr.nabonne.usermessages.common.domain.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalStoreImpl() : LocalStore {
    private val _cachedAllMessages = MutableStateFlow<List<Message>>(emptyList())
    private val _cachedAllMessagesByUser = MutableStateFlow<Map<String, List<Message>>>(emptyMap())
    private val _cachedAllMessagesForAuthor = MutableStateFlow<Pair<String, List<Message>>>(Pair("", emptyList()))
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
        // TODO: Unit test this fancy merge of Immutable maps and list
        val mutableMap = _cachedAllMessagesByUser.value.toMutableMap()
        val existingValues: List<Message> = mutableMap[author] ?: listOf()
        val newValues: List<Message> = if (existingValues.isEmpty())
                messages
            else {
                messages.filter {
                    !existingValues.contains(it)
                }
            }
        val nextValues = existingValues + newValues
        mutableMap[author] = nextValues
        _cachedAllMessagesByUser.value = mutableMap.toMap()
        _cachedAllMessages.value = _cachedAllMessages.value + newValues
        _cachedAllMessagesForAuthor.value = author to nextValues
    }

    override fun getMessagesForAuthor(author: String): StateFlow<Pair<String, List<Message>>> {
        return _cachedAllMessagesForAuthor.asStateFlow()
    }

}