package com.example.eatraw.data

data class Review(
    val content: String,
    val marketName: String,
    val storeImg: String?,
    val storeName: String,
    val rating: Double?,
    val region: String?,
    val like: Int?,
    val fishKind: String?,
    val cost: Int,
    val userId: String?  // 사용자 UID를 추가
)