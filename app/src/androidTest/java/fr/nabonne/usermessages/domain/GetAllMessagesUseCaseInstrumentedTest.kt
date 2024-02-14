package fr.nabonne.usermessages.domain

import app.cash.turbine.test
import fr.nabonne.usermessages.core.domain.data.LocalStore
import fr.nabonne.usermessages.core.domain.data.RemoteApi
import fr.nabonne.usermessages.core.domain.model.Message
import fr.nabonne.usermessages.core.domain.usecases.GetAllMessagesUseCaseImpl
import fr.nabonne.usermessages.core.localstorage.LocalStoreImpl
import fr.nabonne.usermessages.core.network.RemoteApiImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

class GetAllMessagesUseCaseInstrumentedTest {
    val api: RemoteApi = RemoteApiImpl()
    val store: LocalStore = LocalStoreImpl()
    val getAllMessagesUseCase = GetAllMessagesUseCaseImpl(
        remoteApi = api,
        localStore = store
    )

    @Test
    fun bySubjectFlowEmitsInitialValueTest() {
        runBlocking {
            val localStoreFlow: Flow<Map<String, List<Message>>> =
                getAllMessagesUseCase.getAllMessagesBySubject()
            localStoreFlow.test() {
                assertNotNull(awaitItem())
            }
        }
    }

    @Test
    fun bySubjectFlowRefreshTest() {
        runBlocking {
            getAllMessagesUseCase.refresh()
            val localStoreFlow: Flow<Map<String, List<Message>>> =
                getAllMessagesUseCase.getAllMessagesBySubject()
            localStoreFlow.test() {
                assertNotNull(awaitItem())
                ensureAllEventsConsumed()
            }
        }
    }

    @Test
    fun postThenFetchAllMessagesByAuthorTest() {
        val messageModel = Message(
            author = "Dan",
            subject = "instrumented test",
            content = "${UUID.randomUUID()}",
        )
        runBlocking {
            getAllMessagesUseCase.refresh()
            val localStoreFlow: Flow<Map<String, List<Message>>> =
                getAllMessagesUseCase.getAllMessagesByAuthor()
            localStoreFlow.test(
//                timeout = 10.seconds
            ) {
                val initialItem = awaitItem()
                ensureAllEventsConsumed()
                withContext(Dispatchers.IO) {
                    api.postMessage(messageModel)
                }
                delay(500.milliseconds)
                getAllMessagesUseCase.refresh()
                val item = awaitItem()
                assertTrue(
                    item[messageModel.author]?.contains(messageModel) ?: false
                )
                ensureAllEventsConsumed()
            }
        }
    }

    @Test
    fun postThenFetchBySubjectTest() {
        val messageModel = Message(
            author = "Dan",
            subject = "instrumented test",
            content = "${UUID.randomUUID()}",
        )
        runBlocking {
            getAllMessagesUseCase.refresh()
            val localStoreFlow: Flow<Map<String, List<Message>>> =
                getAllMessagesUseCase.getAllMessagesBySubject()
            localStoreFlow.test(
//                timeout = 10.seconds
            ) {
                val initialItem = awaitItem()
                ensureAllEventsConsumed()
                withContext(Dispatchers.IO) {
                    api.postMessage(messageModel)
                }
                delay(500.milliseconds)
                getAllMessagesUseCase.refresh()
                val item = awaitItem()
                assertTrue(
                    item[messageModel.subject]?.contains(messageModel) ?: false
                )
                ensureAllEventsConsumed()
            }
        }
    }

    @Test
    fun postThenFetchInOrderTest() {
        val messageModel = Message(
            author = "Dan",
            subject = "instrumented test",
            content = "${UUID.randomUUID()}",
        )
        runBlocking {
            getAllMessagesUseCase.refresh()
            val localStoreFlow: Flow<List<Message>> =
                getAllMessagesUseCase.getAllMessagesInOrder()
            localStoreFlow.test(
//                timeout = 10.seconds
            ) {
                val initialItem = awaitItem()
                ensureAllEventsConsumed()
                withContext(Dispatchers.IO) {
                    api.postMessage(messageModel)
                }
                delay(500.milliseconds)
                getAllMessagesUseCase.refresh()
                val item = awaitItem()
                assertTrue(
                    item.contains(messageModel) ?: false
                )
                ensureAllEventsConsumed()
            }
        }
    }
}