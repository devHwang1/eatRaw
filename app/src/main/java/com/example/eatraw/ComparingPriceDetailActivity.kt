package com.example.eatraw

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.eatraw.data.ComparingPriceItem
import com.example.eatraw.databinding.ActivityComparingPriceDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

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
            val minCost = extras.getLong("minCost")
            val avgCost = extras.getLong("avgCost")
            val maxCost = extras.getLong("maxCost")
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

        var bnv_main = findViewById(R.id.bnv_main) as BottomNavigationView

        // OnNavigationItemSelectedListener를 통해 탭 아이템 선택 시 이벤트를 처리
        // navi_menu.xml 에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.
        bnv_main.run { setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.first -> {
                    // 다른 액티비티로 이동
                    val intent = Intent(this@ComparingPriceDetailActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.second -> {
                    // 다른 액티비티로 이동
                    val intent = Intent(this@ComparingPriceDetailActivity, ReviewActivity::class.java)
                    startActivity(intent)
                }
                R.id.third -> {
                    // 다른 액티비티로 이동
                    val intent = Intent(this@ComparingPriceDetailActivity, ComparingPriceListActivity::class.java)
                    startActivity(intent)
                }
                R.id.four -> {
                    // 다른 액티비티로 이동
                    val intent = Intent(this@ComparingPriceDetailActivity, MypageActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }}
    }
}