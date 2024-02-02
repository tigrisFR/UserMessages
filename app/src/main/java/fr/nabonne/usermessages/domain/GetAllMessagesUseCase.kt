package fr.nabonne.usermessages.domain

import fr.nabonne.usermessages.domain.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface GetAllMessagesUseCase {
    suspend fun refresh()
    fun getAllMessagesByAuthor(): Flow<Map<String, List<Message>>>

    fun getAllMessagesBySubject(): Flow<Map<String, List<Message>>>

    fun getAllMessagesInOrder(): Flow<List<Message>>
    //TODO: other use-cases like getAllMessagesBySubject getAllMessagesInOrder
}

class GetAllMessagesUseCaseImpl(
    private val remoteApi: RemoteApi,
    private val localStore: LocalStore
): GetAllMessagesUseCase {
    override suspend fun refresh() {
        withContext(Dispatchers.IO) {
            val responseMap = remoteApi.fetchAllMessages()
            transformResponseAndStore(responseMap)
        }
    }

    //TODO perhaps move the computations to Dispatchers.Default
    private suspend fun transformResponseAndStore(responseMap: Map<String, List<MessageResponse>>) {
//            val flattenedMap: List<Message> = responseMap.flatMap { (user, values) ->
//                values.map {
//                    it.toMessageModel(user)
//                }
//            }
        val transformedMap: Map<String, List<Message>> =
            responseMap.mapValues { (user, values) ->
                values.map {
                    it.toMessageModel(user)
                }
            }
        val flattenedTransformedMap: List<Message> = transformedMap.flatMap { (_, values) ->
            values
        }
        storeInLocalStore(transformedMap, flattenedTransformedMap)
    }

    //TODO: pick a lane and decide whether we store a map or a list
    private suspend fun storeInLocalStore(
        transformedMap: Map<String, List<Message>>,
        flattenedTransformedMap: List<Message>
    ) {
        localStore.storeAllMessages(
            allMessagesByUser = transformedMap,
            allMessages = flattenedTransformedMap,
        )
    }

    override fun getAllMessagesByAuthor(): Flow<Map<String, List<Message>>> {
        //TODO: pick a lane and decide whether we get a map or a list from the local store
        return localStore.getAllMessagesByUser()
    }

    override fun getAllMessagesBySubject(): Flow<Map<String, List<Message>>> {
        return localStore.getAllMessages().map { list ->
            list.groupBy {
                it.subject
            }
        }
    }

    override fun getAllMessagesInOrder(): Flow<List<Message>> {
        return localStore.getAllMessages()
    }

}