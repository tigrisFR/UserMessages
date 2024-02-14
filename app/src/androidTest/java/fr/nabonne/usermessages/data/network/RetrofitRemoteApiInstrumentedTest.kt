package fr.nabonne.usermessages.data.network

import fr.nabonne.usermessages.core.network.PostMessageRequest
import fr.nabonne.usermessages.core.network.RemoteApiImpl
import fr.nabonne.usermessages.core.network.RetrofitRemoteApi
import fr.nabonne.usermessages.core.network.model.MessageResponse
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID

class RetrofitRemoteApiInstrumentedTest {
    val api: RetrofitRemoteApi = RemoteApiImpl().retrofitRemoteApi

    @Test
    fun postMessageTest() {
        val messageUniqueContent = UUID.randomUUID()
        val postMessageRequest = PostMessageRequest(
            user = "dan",
            subject = "pets",
            message = "cats are grumpy $messageUniqueContent",
        )
        runBlocking {
            val response = api.postMessage(postMessageRequest)
            println("Response: ${response.raw()}")
            assertTrue(response.isSuccessful)
            assertNotNull(response.body())
            //TODO figure out why response body is not deserialized correctly
//            assertEquals("dan", response.body()!!.user)
//            assertEquals("pets2", response.body()!!.subject)
//            assertEquals("cats are grumpy $messageUniqueContent", response.body()!!.message)
        }
    }

    @Test
    fun getAllUserMessagesTest() {
        runTest {
            val response = api.getAllUserToMessagesMap()
            println("Response: $response")
            assertEquals(200, response.statusCode)
        }
    }

    @Test
    fun getAllUserMessagesAfterPostTest() {
        val messageUniqueContent = UUID.randomUUID()
        val user = "Bob"
        val subject = "boats"
        val message = "boats are floating $messageUniqueContent"
        val postMessageRequest = PostMessageRequest(
            user = user,
            subject = subject,
            message = message,
        )
        val expectedMessage = MessageResponse(
            subject = subject,
            message = message
        )
        runBlocking {
            val postResponse = api.postMessage(postMessageRequest)
            println("Response: ${postResponse.raw()}")
            assertTrue(postResponse.isSuccessful)
            assertNotNull(postResponse.body())

            val getResponse = api.getAllUserToMessagesMap()
            println("Response: $getResponse, map=${getResponse.body}")
            assertEquals(200, getResponse.statusCode)
            val userMessageMap = getResponse.body
            assertNotNull(userMessageMap)
            assertTrue(userMessageMap[user]?.contains(expectedMessage) ?: false)
        }
    }
}