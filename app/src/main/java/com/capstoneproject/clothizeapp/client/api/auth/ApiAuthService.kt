package com.capstoneproject.clothizeapp.client.api.auth

import retrofit2.http.Field
import retrofit2.http.POST

interface ApiAuthService {

    @POST("update")
    suspend fun updateUserData(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): String
//
//
//    @Multipart
//    @POST("stories")
//    suspend fun uploadStory(
//    ): String

}