package com.vozmediano.vozmedianonasa.data

import com.vozmediano.vozmedianonasa.data.network.api.PhotoService
import com.vozmediano.vozmedianonasa.domain.PhotoRepository
import com.vozmediano.vozmedianonasa.domain.model.Photo

class PhotoRepositoryImpl(private val photoService: PhotoService) : PhotoRepository {

    override suspend fun fetchPhotos(startDate:String,endDate:String): List<Photo> {
        val photosResponse = photoService.getPhotos(startDate, endDate)
        return photosResponse.map { photo -> photo.toDomain() }
    }

    override suspend fun fetchPhoto(): Photo {
        val photoResponse = photoService.getPhoto()
        return photoResponse.toDomain()
    }

    override suspend fun fetchPhoto(date: String): Photo {
        val photoResponse = photoService.getPhoto(date)
        return photoResponse.toDomain()
    }

}
