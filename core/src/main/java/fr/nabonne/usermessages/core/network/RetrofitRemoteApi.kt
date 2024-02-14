package fr.nabonne.usermessages.core.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface RetrofitRemoteApi {

    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("/proto/messages")
    suspend fun postMessage(@Body request: PostMessageRequest): Response<PostMessageResponse>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("/proto/messages")
    suspend fun getAllUserToMessagesMap(): UserMessagesResponse

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET
    suspend fun getMessagesForUserUrlSuffix(@Url relativeUrlWithUserName: String): GetMessagesForAuthorResponse
}