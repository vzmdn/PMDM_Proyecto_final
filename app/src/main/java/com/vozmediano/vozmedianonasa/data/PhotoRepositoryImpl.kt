package com.vozmediano.vozmedianonasa.data

import android.util.Log
import com.vozmediano.vozmedianonasa.data.local.dao.PhotoDao
import com.vozmediano.vozmedianonasa.data.network.api.PhotoService
import com.vozmediano.vozmedianonasa.domain.PhotoRepository
import com.vozmediano.vozmedianonasa.domain.model.Photo
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

class PhotoRepositoryImpl(
    private val photoDao: PhotoDao,
    private val photoService: PhotoService
) : PhotoRepository {

    override suspend fun fetchPhotos(startDate: String, endDate: String): List<Photo> {
        return try {
            val photosResponse = photoService.getPhotos(startDate, endDate)
            val photos = photosResponse.map { it.toDomain() }
            photoDao.insertAll(photos.map { it.toDatabase() })
            photos
        } catch (ex: Exception) {
            Log.d("Tests", ex.message.orEmpty())
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val startDate = LocalDate.now().minusDays(8).format(formatter)
            val endDate = LocalDate.now().minusDays(1).format(formatter)
            photoDao.getAll(startDate,endDate).map { it.toDomain() }
        }
    }

    override suspend fun fetchPhoto(): Photo {
        return try {
            val photoResponse = photoService.getPhoto()
            val photo = photoResponse.toDomain()
            photoDao.insertAll(listOf(photo.toDatabase()))
            photo
        } catch (ex: Exception) {
            Log.d("Tests", ex.message.orEmpty())
            photoDao.getPhotoByDate(LocalDate.now().toString()).first().toDomain()
        }
    }

    override suspend fun fetchPhoto(date: String): Photo {
    return try {
        val photoResponse = photoService.getPhoto(date)
        val photo = photoResponse.toDomain()
        photoDao.insertAll(listOf(photo.toDatabase()))
        photo
    } catch (ex: Exception) {
        Log.d("Tests", "Error fetching photo from network: ${ex.message.orEmpty()}")
        val photoFromDb = photoDao.getPhotoByDate(date).first().toDomain()
        photoFromDb
    }
}
}