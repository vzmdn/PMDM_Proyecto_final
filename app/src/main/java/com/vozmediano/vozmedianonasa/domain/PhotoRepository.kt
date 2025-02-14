package com.vozmediano.vozmedianonasa.domain

import com.vozmediano.vozmedianonasa.domain.model.Photo

interface PhotoRepository {
    suspend fun fetchPhotos(startDate: String, endDate: String): List<Photo>
    suspend fun fetchPhoto(date: String): Photo
}
