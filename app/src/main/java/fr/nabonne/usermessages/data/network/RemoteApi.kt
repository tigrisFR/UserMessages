package fr.nabonne.usermessages.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface RemoteApi {

    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("/proto/messages")
    suspend fun postMessage(@Body request: PostMessageRequest): Response<PostMessageResponse>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("/proto/messages")
    suspend fun getAllUserToMessagesMap(): UserMessagesResponse
}