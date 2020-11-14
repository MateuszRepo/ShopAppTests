package com.mateuszb.shopapptests.data.remote.models

data class ImageResponse(
    val hits: List<ImageResult>,
    val total: Int,
    val totalHits: Int
)