package com.example.eatraw.data

data class Review(
    val content: String,
    val marketName: String,
    val storeImg: String?,
    val storeName: String,
    val rating: Double?
)
