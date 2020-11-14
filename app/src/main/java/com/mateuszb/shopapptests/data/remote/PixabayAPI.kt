package com.mateuszb.shopapptests.data.remote

import com.mateuszb.shopapptests.BuildConfig
import com.mateuszb.shopapptests.data.remote.models.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayAPI {

    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): Response<ImageResponse>
}