package com.example.eatraw.data

data class Review(
    val content: String,    //리뷰
    val marketName: String, //시장이름
    val storeImg: String?,  //리뷰이미지(썸네일겸용)
    val storeName: String,  //가게이름
    val rating: Double?,    //별점
    val region: String?,    //지역
    val like: Int?,         //좋아요
    val fishKind: Int?,     //고기종류
    val cost: String?       //가격


)
