package com.example.eatraw.data

import com.google.firebase.Timestamp

data class Review(
    val content: String,
    val marketName: String,
    val storeImg: String?,
    val storeName: String,
    val rating: Double?,
    val region: String?,
    val like: Int?,
    val fishKind: String?,
    val cost: Int?,
    val userId: String?,  // 사용자 UID를 추가
    val reviewId: String? = null, //  리뷰 고유 아이디 추가
    val timestamp: Timestamp? = null
)


