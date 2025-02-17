package com.vozmediano.vozmedianonasa.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "photos")
data class PhotoEntity (
    @PrimaryKey val date : String,
    val explanation : String,
    val hdurl : String,
    val title : String,
    val url : String,
    val media_type : String
)