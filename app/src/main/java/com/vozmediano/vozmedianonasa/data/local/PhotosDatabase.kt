package com.vozmediano.vozmedianonasa.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vozmediano.vozmedianonasa.data.local.dao.PhotoDao
import com.vozmediano.vozmedianonasa.data.local.entities.PhotoEntity

@Database(entities = [PhotoEntity::class], version = 1)
abstract class PhotosDatabase : RoomDatabase() {
    abstract fun photoDao() : PhotoDao
}