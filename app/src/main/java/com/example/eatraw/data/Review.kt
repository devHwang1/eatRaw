package com.example.eatraw.data

data class Review(
<<<<<<< HEAD
    val content: String,    //리뷰
    val marketName: String, //시장이름
    val storeImg: String?,  //리뷰이미지(썸네일겸용)
    val storeName: String,  //가게이름
    val rating: Double?,    //별점
    val region: String?,    //지역
    val like: Int?,         //좋아요
    val fishKind: Int?,     //고기종류
    val cost: String?       //가격


=======
    val content: String,
    val marketName: String,
    val storeImg: String?,
    val storeName: String,
    val rating: Double?,
    val region: String?,
    val like: Int?,
    val fishKind: Int?,
    val cost: String?,
    val userId: String?  // 사용자 UID를 추가
>>>>>>> e83978d81e46812c888fafade304e3cc29213a84
)
