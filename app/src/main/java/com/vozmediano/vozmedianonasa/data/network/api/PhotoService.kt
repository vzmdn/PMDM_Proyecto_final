package com.vozmediano.vozmedianonasa.data.network.api

import com.vozmediano.vozmedianonasa.data.network.model.PhotoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoService {

    @GET("apod")
    suspend fun getPhotos(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): List<PhotoResponse>

    @GET("apod")
    suspend fun getPhoto(
        @Query("date") date: String
    ): PhotoResponse


}
