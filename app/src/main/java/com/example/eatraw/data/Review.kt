package com.example.eatraw.data

import com.google.firebase.storage.StorageReference

data class Review(
    val content: String,
    val marketName: String,
    val storeImg: String?,
    val storeName: String,
    val rating: Double?,
    val region: String?,
    val like: Int?,
    val fishKind: Int?,
    val cost: String?,
    val userId: String?,  // 사용자 UID를 추가
)
