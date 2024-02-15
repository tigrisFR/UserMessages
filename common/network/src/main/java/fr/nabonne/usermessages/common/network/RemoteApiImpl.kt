package fr.nabonne.usermessages.common.network

import android.accounts.NetworkErrorException
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.nabonne.usermessages.common.domain.data.RemoteApi
import fr.nabonne.usermessages.common.domain.model.Message
import fr.nabonne.usermessages.common.domain.data.model.MessageResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteApiImpl() : RemoteApi {
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(
            UserMessagesResponse::class.java,
            UserMessagesResponseDeserializer()
        )
        .registerTypeAdapter(
            GetMessagesForAuthorResponse::class.java,
            GetMessagesForAuthorResponseDeserializer()
        )
        .create()
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    //not private as we need to expose to instrumented tests
    val retrofitRemoteApi: RetrofitRemoteApi by lazy {
        retrofit.create(RetrofitRemoteApi::class.java)
    }

    companion object {
        const val BASE_URL = "https://abraxvasbh.execute-api.us-east-2.amazonaws.com/"
    }

    //TODO better network error reporting
    //TODO properly parse the String in the Response body
    override suspend fun postMessage(message: Message) {
        val response = retrofitRemoteApi.postMessage(
            PostMessageRequest(
                user = message.author,
                subject = message.subject,
                message = message.content
            )
        )
        if (!response.isSuccessful) {
            throw NetworkErrorException("Error postMessage: ${response.code()}")
        }
    }

    override suspend fun fetchAllMessages(): Map<String, List<MessageResponse>> {
        val response = retrofitRemoteApi.getAllUserToMessagesMap()

        if (200 != response.statusCode) {
            throw NetworkErrorException("Error fetchAllMessages: ${response}")
        }
//        return response.body.mapValues { (user, values) ->
//            values.map { it.toMessageModel(user)}
//        } ?: emptyMap<String, List<Message>>()
        return response.body
    }

    override suspend fun fetchMessagesForUser(user: String): List<MessageResponse> {
        val response = retrofitRemoteApi.getMessagesForUserUrlSuffix(
            "/proto/messages/$user"
        )

        return if (404 == response.statusCode) {
            emptyList()
        } else if (200 == response.statusCode) {
            if (user != response.user) {
                throw NetworkErrorException("Error fetchMessagesForUser($user) wrong user: ${response}")
            }
            return response.messages ?: emptyList()
        } else {
            throw NetworkErrorException("Error fetchMessagesForUser($user): ${response}")
        }
    }
}