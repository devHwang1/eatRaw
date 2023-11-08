package com.example.eatraw.data

data class BestReviewItem(
    val imageResId: String?, // 이미지 리소스 식별자
    val rating: Double?,   // 별점 (예: 4.5)
    val likeCount: Long    // 'like' 수
)
