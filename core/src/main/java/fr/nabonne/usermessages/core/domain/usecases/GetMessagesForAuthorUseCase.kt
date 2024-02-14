package fr.nabonne.usermessages.core.domain.usecases

import fr.nabonne.usermessages.core.data.modelconversion.toMessageModel
import fr.nabonne.usermessages.core.domain.data.LocalStore
import fr.nabonne.usermessages.core.domain.data.RemoteApi
import fr.nabonne.usermessages.core.domain.model.Message
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
            val result = remoteApi.fetchMessagesForUser(author)
            storeResults(author, result.map { it.toMessageModel(author) })
        }
    }

    private suspend fun storeResults(author: String, messages: List<Message>) {
        localStore.storeMessagesForAuthor(author, messages)
    }

    override fun getMessagesForAuthor(author: String): Flow<Pair<String, List<Message>>> {
        return localStore.getMessagesForAuthor(author)
    }
}