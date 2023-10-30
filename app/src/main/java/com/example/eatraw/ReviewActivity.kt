package com.example.eatraw

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatraw.adapter.ReviewAdapter
import com.example.eatraw.databinding.ActivityMainBinding
import com.example.eatraw.databinding.ActivityReviewBinding

class ReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewBinding
    private lateinit var spinner: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var spinner3: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReviewAdapter // ReviewAdapter는 나중에 만들 예정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinner = binding.spinner1
        spinner2 = binding.spinner2
        recyclerView = binding.reviewRecycler

        // RecyclerView의 레이아웃 매니저 설정 (리스트 형태로 보이도록 설정)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 어댑터 초기화
        adapter = ReviewAdapter() // ReviewAdapter는 나중에 만들 예정

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
}