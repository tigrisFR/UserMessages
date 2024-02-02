package fr.nabonne.usermessages.domain

import fr.nabonne.usermessages.domain.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface GetMessagesForAuthorUseCase {
    suspend fun refreshForUser(author: String)
    fun getMessagesForAuthor(author: String): Flow<Pair<String, List<Message>>>
}

class GetMessagesForAuthorUseCaseImpl(
    private val remoteApi: RemoteApi,
    private val localStore: LocalStore,
) : GetMessagesForAuthorUseCase {

    override suspend fun refreshForUser(author: String) {
        withContext(Dispatchers.IO) {
            val response = remoteApi.fetchMessagesForUser(author)
            storeResults(author, response)
        }
    }

    private suspend fun storeResults(author: String, messages: List<Message>) {
        localStore.storeMessagesForAuthor(author, messages)
    }

    override fun getMessagesForAuthor(author: String): Flow<Pair<String, List<Message>>> {
        return localStore.getMessagesForAuthor(author)
    }
}