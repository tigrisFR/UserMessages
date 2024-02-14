package fr.nabonne.usermessages.domain

import app.cash.turbine.test
import fr.nabonne.usermessages.core.domain.data.LocalStore
import fr.nabonne.usermessages.core.domain.data.RemoteApi
import fr.nabonne.usermessages.core.domain.model.Message
import fr.nabonne.usermessages.core.domain.usecases.GetMessagesForAuthorUseCaseImpl
import fr.nabonne.usermessages.core.localstorage.LocalStoreImpl
import fr.nabonne.usermessages.core.network.RemoteApiImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert
import org.junit.Test
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class GetMessagesForAuthorUseCaseInstrumentedTest {

    val api: RemoteApi = RemoteApiImpl()
    val store: LocalStore = LocalStoreImpl()
    val usecase = GetMessagesForAuthorUseCaseImpl(
        remoteApi = api,
        localStore = store
    )

    @Test
    fun bySubjectFlowEmitsInitialValueTest() {
        runBlocking {
            val localStoreFlow: Flow<Pair<String, List<Message>>> =
                usecase.getMessagesForAuthor(UUID.randomUUID().toString())
            localStoreFlow.test() {
                Assert.assertNotNull(awaitItem())
            }
        }
    }

    @Test
    fun postThenFetchByAuthorTest() {
        val messageModel = Message(
            author = "Dan",
            subject = "instrumented test",
            content = "${UUID.randomUUID()}",
        )
        runBlocking {
            usecase.refreshForUser(messageModel.author)
            val localStoreFlow: Flow<Pair<String, List<Message>>> =
                usecase.getMessagesForAuthor(messageModel.author)
            localStoreFlow.test(
                timeout = 10.seconds
            ) {
                val initialItem = awaitItem()
                ensureAllEventsConsumed()
                withContext(Dispatchers.IO) {
                    api.postMessage(messageModel)
                }
                delay(500.milliseconds)
                usecase.refreshForUser(messageModel.author)
                val item = awaitItem()
                Assert.assertTrue(
                    item.first == messageModel.author
                )
                Assert.assertTrue(
                    item.second.contains(messageModel) ?: false
                )
                ensureAllEventsConsumed()
            }
        }
    }
}