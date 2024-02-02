package fr.nabonne.usermessages.data.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteApiProvider() {
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(UserMessagesResponse::class.java, UserMessagesResponseDeserializer())
        .create()
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Companion.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    val remoteApi: RemoteApi by lazy {
        retrofit.create(RemoteApi::class.java)
    }

    companion object {
        const val BASE_URL = "https://abraxvasbh.execute-api.us-east-2.amazonaws.com/"
    }
}