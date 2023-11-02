package com.example.eatraw.data

data class Review(
    val content: String,
    val marketName: String,
    val storeImg: String?,
    val storeName: String,
    val rating: Double?,
<<<<<<< HEAD
    val region : String?

=======
    val region: String?,
    val like: Int?,
    val fishKind: Int?,
    val cost: String?
>>>>>>> c7ff4fba6eae846c2dd43a76ffe74710b7200ce8
)
