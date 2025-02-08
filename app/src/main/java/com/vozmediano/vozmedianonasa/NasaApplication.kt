package com.vozmediano.vozmedianonasa

import android.app.Application
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.vozmediano.vozmedianonasa.data.PhotoRepositoryImpl
import com.vozmediano.vozmedianonasa.data.network.RequestTokenInterceptor
import com.vozmediano.vozmedianonasa.data.network.api.PhotoService
import com.vozmediano.vozmedianonasa.domain.PhotoRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class NasaApplication : Application() {
    val API_TOKEN = "DEMO_KEY"

    lateinit var photoRepository : PhotoRepository

    private fun getPhotoService(token: String): PhotoService {
        val client = OkHttpClient.Builder()
            .addInterceptor(RequestTokenInterceptor(token))
            .build()

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/planetary/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val photoService = retrofit.create(PhotoService::class.java)
        return photoService
    }

    override fun onCreate() {
        super.onCreate()
        val service = getPhotoService(API_TOKEN)

        photoRepository = PhotoRepositoryImpl(service)
    }

}