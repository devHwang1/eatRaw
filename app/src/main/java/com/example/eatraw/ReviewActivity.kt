package com.example.eatraw

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatraw.adapter.ReviewAdapter
<<<<<<< HEAD
import com.example.eatraw.databinding.ActivityMainBinding
=======
import com.example.eatraw.data.Review
>>>>>>> e1fa736bb52265df2587a8f0f4b89b882dac2b35
import com.example.eatraw.databinding.ActivityReviewBinding

class ReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewBinding
    private lateinit var spinner: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinner = binding.spinner1
        spinner2 = binding.spinner2
        recyclerView = binding.reviewRecycler

        // RecyclerView의 레이아웃 매니저 설정
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 리뷰 데이터를 생성
        val reviewData = createReviewData()

        // 어댑터 초기화
        adapter = ReviewAdapter(reviewData)

        // RecyclerView에 어댑터 설정
        recyclerView.adapter = adapter

        val items = arrayOf("서울", "부산", "대구", "경남", "경북", "충남")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(1)

        val items2 = arrayOf("자갈치수산", "은하수산", "대명수산", "우리수산", "부산수산", "미리수산", "양어수산")
        val adapter2 = ArrayAdapter(this, R.layout.simple_spinner_item, items2)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = adapter2
        spinner2.setSelection(0)
    }
<<<<<<< HEAD
}
=======

    // 리뷰 데이터를 생성하는 함수
    private fun createReviewData(): List<Review> {
        val reviews = mutableListOf<Review>()
        reviews.add(Review(R.drawable.ic_launcher_background, "참말로 싸고 맛있네예 담에 또 올게예~", R.drawable.reviewstar, "4.9", "자갈치시장", "광어"))
        // 다른 리뷰 데이터도 추가 가능
        return reviews
    }
}
>>>>>>> e1fa736bb52265df2587a8f0f4b89b882dac2b35
