package com.example.advancedandroidproject.Network
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NationalParkServiceApi {

    @GET("parks/")
    suspend fun getParks(
        @Query("api_key") apiKey: String,
        @Query("stateCode") stateCode: String
    ): Response<ParksResponse>
}
