package com.example.eatraw.data


data class Users(
    val email: String,
    val nickname: String,
    val aouthLogin: Boolean,
    var admin: Boolean,
    val imageUrl: String?,
    val userId: String,
    val likeMarket: String?
)

