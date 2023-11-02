package com.example.eatraw

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatraw.adapter.BannerAdapter
import com.example.eatraw.adapter.BestReviewAdapter
import com.example.eatraw.adapter.ComparingPriceAdapter
import com.example.eatraw.data.BannerItem
import com.example.eatraw.data.BestReviewItem
import com.example.eatraw.data.ComparingPriceItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var button: Button
    private lateinit var textView: TextView
    private var user: FirebaseUser? = null

    private lateinit var recyclerViewBanner: RecyclerView
    private lateinit var recyclerViewBestReview: RecyclerView
    private lateinit var recyclerViewComparingPrice: RecyclerView

    // 예시 데이터를 생성합니다.
    private val bannerData: List<BannerItem> = listOf(
        BannerItem(R.drawable.banner1),
        BannerItem(R.drawable.banner2),
        BannerItem(R.drawable.banner1),
        BannerItem(R.drawable.banner2),
        BannerItem(R.drawable.banner1),
        BannerItem(R.drawable.banner2)
        // 추가적인 BannerItem 인스턴스와 설명, 타이틀을 추가하세요.
    )

    private val bestReviewData: List<BestReviewItem> = listOf(
        BestReviewItem(R.drawable.review1, 4.5),
        BestReviewItem(R.drawable.review2, 4.0),
        BestReviewItem(R.drawable.review1, 4.3),
        BestReviewItem(R.drawable.review2, 4.2),
        BestReviewItem(R.drawable.review1, 4.1),
        BestReviewItem(R.drawable.review2, 4.6),
        BestReviewItem(R.drawable.review1, 4.7),
        BestReviewItem(R.drawable.review2, 4.8)
        // 추가적인 BestReviewItem 인스턴스와 이미지, 평점을 추가하세요.
    )

    private val comparingPriceData: List<ComparingPriceItem> = listOf(
        ComparingPriceItem("오징어", "10,000"),
        ComparingPriceItem("꼴뚜기", "11,000"),
        ComparingPriceItem("대구", "20,000"),
        ComparingPriceItem("명태", "5,000"),
        ComparingPriceItem("거북이", "110,000"),
        ComparingPriceItem("연어알", "30,000"),
        ComparingPriceItem("물새알", "50,000"),
        ComparingPriceItem("연어", "30,000"),
        ComparingPriceItem("대합실", "6,000"),
        ComparingPriceItem("오징어", "10,000"),
        ComparingPriceItem("꼴뚜기", "11,000"),
        ComparingPriceItem("대구", "20,000"),
        ComparingPriceItem("명태", "5,000"),
        ComparingPriceItem("거북이", "110,000"),
        ComparingPriceItem("연어알", "30,000"),
        ComparingPriceItem("물새알", "50,000"),
        ComparingPriceItem("연어", "30,000"),
        ComparingPriceItem("대합실", "6,000"),


        // 추가적인 ComparingPriceItem 인스턴스와 생선 이름, 가격을 추가하세요.
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // RecyclerView 초기화
        recyclerViewBanner = findViewById(R.id.recyclerViewBanner)
        recyclerViewBestReview = findViewById(R.id.recyclerViewBestReview)
        recyclerViewComparingPrice = findViewById(R.id.recyclerViewComparingPrice)

        // 레이아웃 매니저 설정
        val layoutManagerBanner = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerBestReview = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerComparingPrice = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recyclerViewBanner.layoutManager = layoutManagerBanner
        recyclerViewBestReview.layoutManager = layoutManagerBestReview
        recyclerViewComparingPrice.layoutManager = layoutManagerComparingPrice

        // 어댑터 설정
        val adapterBanner = BannerAdapter(bannerData)
        val adapterBestReview = BestReviewAdapter(bestReviewData)
        val adapterComparingPrice = ComparingPriceAdapter(comparingPriceData)

        recyclerViewBanner.adapter = adapterBanner
        recyclerViewBestReview.adapter = adapterBestReview
        recyclerViewComparingPrice.adapter = adapterComparingPrice

        // 로그인 설정
        auth = FirebaseAuth.getInstance()
        button = findViewById(R.id.logout)
        textView = findViewById(R.id.user_details)
        user = auth.currentUser

        if (user == null) {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            textView.text = user?.email
        }

        button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        // ComparingPriceListActivity로 넘김
        val comparingPriceItems: List<ComparingPriceItem> = comparingPriceData.toList()
        val seeingMoreTextView = findViewById<TextView>(R.id.seeingMore)
        seeingMoreTextView.setOnClickListener {
            val intent = Intent(this, ComparingPriceListActivity::class.java)

            // comparingPriceItems를 ComparingPriceListActivity로 전달
            intent.putParcelableArrayListExtra("comparingPriceItems", ArrayList(comparingPriceItems))
            startActivity(intent)
        }


    }
}