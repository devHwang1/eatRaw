package com.example.eatraw

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.eatraw.data.ComparingPriceItem
import com.example.eatraw.databinding.ActivityComparingPriceDetailBinding

class ComparingPriceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComparingPriceDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComparingPriceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 전달된 정보 받기
        val extras = intent.extras
        if (extras != null) {
            val fishName = extras.getString("fishName")
            val minCost = extras.getString("minCost")
            val avgCost = extras.getString("avgCost")
            val maxCost = extras.getString("maxCost")
            var fishImg = extras.getString("fishImg")

            // 받아온 정보를 화면에 표시
            binding.fishNameDetail.text = fishName
            binding.fishPriceMax.text = "$maxCost 원"
            binding.fishPriceMin.text = "$minCost 원"
            binding.fishPriceAvg.text = "$avgCost 원"
            Glide.with(this)
                .load(fishImg) // 이미지 URL 설정
                .into(binding.fishImg) // ImageView에 이미지 표시
        }

        // 뒤로가기 버튼 처리
        val backButton = binding.imgBackarrow
        backButton.setOnClickListener {
            onBackPressed()
        }

        // WriteActivity로
        val buyReview = binding.btnBuyReview
        buyReview.setOnClickListener {
            val intent = Intent(this, WriteActivity::class.java)
//            intent.putExtra("fishNameDetail", fishNameDetail)
            startActivity(intent)
        }
    }
}
