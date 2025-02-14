package com.vozmediano.vozmedianonasa.data

import android.util.Log
import com.vozmediano.vozmedianonasa.data.local.dao.PhotoDao
import com.vozmediano.vozmedianonasa.data.network.api.PhotoService
import com.vozmediano.vozmedianonasa.domain.PhotoRepository
import com.vozmediano.vozmedianonasa.domain.model.Photo
import kotlinx.coroutines.flow.first

class PhotoRepositoryImpl(
    private val photoDao: PhotoDao,
    private val photoService: PhotoService
) : PhotoRepository {

    override suspend fun fetchPhotos(startDate: String, endDate: String): List<Photo> {
        return try {
            val cachedPhotos = photoDao.getAll(startDate, endDate).map { it.toDomain() }
            if (cachedPhotos.isNotEmpty()) {
                Log.i("Tests", "Photos from cache: $cachedPhotos")
                cachedPhotos
            } else {
                throw Exception("Cache is empty")
            }

        } catch (ex: Exception) {
            Log.d("Tests", ex.message.orEmpty())
            val photosResponse = photoService.getPhotos(startDate, endDate)
            val photos = photosResponse.map { it.toDomain() }
            photoDao.insertAll(photos.map { it.toDatabase() })
            photos
        }
    }


    override suspend fun fetchPhoto(date: String): Photo {
        return try {
            val cachedPhoto = photoDao.getPhotoByDate(date).first().toDomain()
            cachedPhoto

        } catch (ex: Exception) {
            Log.d("Tests", "Error fetching photo from network: ${ex.message.orEmpty()}")
            val photoResponse = photoService.getPhoto(date)
            val photo = photoResponse.toDomain()
            photoDao.insertAll(listOf(photo.toDatabase()))
            photo
        }
    }
}
