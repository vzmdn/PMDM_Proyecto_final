package com.vozmediano.vozmedianonasa.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.vozmediano.vozmedianonasa.data.local.entities.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getAll(startDate: String, endDate: String): List<PhotoEntity>

    @Query("SELECT * FROM photos WHERE date = :date")
    fun getPhotoByDate(date: String): Flow<PhotoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<PhotoEntity>)

    @Query("DELETE FROM photos")
    suspend fun clearAll()

    @Upsert
    suspend fun upsertAll(photos: List<PhotoEntity>)


}