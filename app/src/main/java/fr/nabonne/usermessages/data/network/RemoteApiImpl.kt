package fr.nabonne.usermessages.data.network

import android.accounts.NetworkErrorException
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.nabonne.usermessages.domain.MessageResponse
import fr.nabonne.usermessages.domain.RemoteApi
import fr.nabonne.usermessages.domain.model.Message
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteApiImpl() : RemoteApi {
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(UserMessagesResponse::class.java, UserMessagesResponseDeserializer())
        .create()
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Companion.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    //not private as we need to expose to instrumented tests
    internal val retrofitRemoteApi: RetrofitRemoteApi by lazy {
        retrofit.create(RetrofitRemoteApi::class.java)
    }

    companion object {
        const val BASE_URL = "https://abraxvasbh.execute-api.us-east-2.amazonaws.com/"
    }

    //TODO better network error reporting
    override suspend fun postMessage(message: Message) {
        val response = retrofitRemoteApi.postMessage(
            PostMessageRequest(
                user = message.author,
                subject = message.subject,
                message = message.content
            )
        )
        if (!response.isSuccessful) {
            throw NetworkErrorException("An network error occurred, code=${response.code()}")
        }
    }

    override suspend fun fetchAllMessages(): Map<String, List<MessageResponse>> {
        val response = retrofitRemoteApi.getAllUserToMessagesMap()

        if (200 != response.statusCode) {
            throw NetworkErrorException("An network error occurred, code=${response.statusCode}")
        }
//        return response.body.mapValues { (user, values) ->
//            values.map { it.toMessageModel(user)}
//        } ?: emptyMap<String, List<Message>>()
        return response.body
    }

    override suspend fun fetchMessagesForUser(user: String): List<Message> {
        //TODO
        return emptyList()
    }
}