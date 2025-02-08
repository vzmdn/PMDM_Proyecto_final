package com.vozmediano.vozmedianonasa.data

import com.vozmediano.vozmedianonasa.data.network.model.PhotoResponse
import com.vozmediano.vozmedianonasa.domain.model.Photo


fun PhotoResponse.toDomain() =
    Photo(
        date,
        explanation ?: "",
        hdurl ?: "",
        title ?: "",
        url ?: ""
    )