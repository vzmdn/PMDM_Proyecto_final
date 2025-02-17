package com.vozmediano.vozmedianonasa.data

import com.vozmediano.vozmedianonasa.data.local.entities.PhotoEntity
import com.vozmediano.vozmedianonasa.data.network.model.PhotoResponse
import com.vozmediano.vozmedianonasa.domain.model.Photo

fun PhotoResponse.toDomain() = Photo(
    date = date,
    explanation = explanation ?: "",
    hdurl = hdurl ?: "",
    title = title ?: "",
    url = url ?: "",
    mediaType = media_type ?: ""
)

fun Photo.toDatabase() = PhotoEntity(
    date = date,
    explanation = explanation,
    hdurl = hdurl,
    title = title,
    url = url,
    media_type = mediaType
)

fun PhotoEntity.toDomain() = Photo(
    date = date,
    explanation = explanation,
    hdurl = hdurl,
    title = title,
    url = url,
    mediaType = media_type
)