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
<<<<<<< HEAD
    val fishKind: Int?,
    val cost: String?,
    val userId: String?,  // 사용자 UID를 추가
=======
    val fishKind: String,
    val cost: Int,
    val userId: String?  // 사용자 UID를 추가
<<<<<<< HEAD
)
=======
>>>>>>> e6343df945f4d1bf2f683aaffc2611f55bbb3128
)
>>>>>>> 31b06537f7e25bbdd9f616e623a1b594fd74ee8d
