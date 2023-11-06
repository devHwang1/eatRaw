package com.example.eatraw

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatraw.adapter.BannerAdapter
import com.example.eatraw.adapter.BestReviewAdapter
import com.example.eatraw.adapter.ComparingPriceAdapter
import com.example.eatraw.data.BannerItem
import com.example.eatraw.data.BestReviewItem
import com.example.eatraw.data.ComparingPriceItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var button: Button
    private lateinit var textView: TextView
    private var user: FirebaseUser? = null

    private lateinit var recyclerViewBanner: RecyclerView
    private lateinit var recyclerViewBestReview: RecyclerView
    private lateinit var recyclerViewComparingPrice: RecyclerView


    // Firebase Firestore 인스턴스 가져오기
    private val firestore = FirebaseFirestore.getInstance()

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

    private val bestReviewData: MutableList<BestReviewItem> = mutableListOf()

    private val comparingPriceData: List<ComparingPriceItem> = listOf(

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
        val layoutManagerBestReview =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerComparingPrice =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

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
        user = auth.currentUser

        if (user == null) {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        // 파이어베이스에서 좋아요 순으로 리뷰를 가져오는 코드
        firestore.collection("review")
            .orderBy("rating", Query.Direction.DESCENDING) // 좋아요 수를 내림차순으로 정렬
            .limit(10) // 상위 10개 아이템만 가져옴
            .get()
            .addOnSuccessListener { documents ->
                bestReviewData.clear() // 목록을 채우기 전에 목록을 지웁니다

                for (document in documents) {
                    val ratingField = document["rating"]
                    val rating = if (ratingField is Number) {
                        ratingField.toDouble()
                    } else {
                        0.0
                    }
                    val likeField = document["like"]
                    val like = if (likeField is Number) {
                        likeField.toLong()
                    } else {
                        0L
                    }

                    val reviewImage = document.getString("storeImg")

                    if (reviewImage != null) {
                        // 이미지가 있는 경우 Glide로 이미지 로드
                        bestReviewData.add(BestReviewItem(reviewImage, rating, like))
                    } else {
                        // 이미지가 없는 경우 기본 이미지로 처리
                        val defaultImageResourceId = R.drawable.default_nallo.toString() // default_image는 기본 이미지의 리소스 ID입니다
                        bestReviewData.add(BestReviewItem(defaultImageResourceId, rating, like))
                    }
                }

                // 어댑터에 데이터가 변경되었음을 알립니다
                adapterBestReview.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // 데이터를 가져오지 못한 경우 처리
                Log.e("FirestoreError", "문서 가져오기 오류: ", exception)
            }


        // 파이어베이스에서 물고기 목록을 가져오는 코드
        firestore.collection("fish")
            .get()
            .addOnSuccessListener { documents ->
                val comparingPriceData = mutableListOf<ComparingPriceItem>()

                for (document in documents) {
                    val fishName = document.getString("f_name")
                    val minCost = (document["f_min"] as? Long)?.toInt()
                    val avgCost = (document["f_avg"] as? Long)?.toInt()
                    val maxCost = (document["f_max"] as? Long)?.toInt()
                    val fishImg = document.getString("f_img")
                    val season = document.getString("f_season")
                    val storageReference = FirebaseStorage.getInstance().reference
                    val imageRef = storageReference.child("FishImg/$fishImg")

                    if (fishName != null && minCost != null && avgCost != null && maxCost != null) {
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()
                            val comparingPriceItem = ComparingPriceItem(
                                fishName,
                                minCost.toString(),
                                avgCost.toString(),
                                maxCost.toString(),
                                imageUrl,
                                season
                            )
                            comparingPriceData.add(comparingPriceItem)

                            // 데이터를 RecyclerView에 설정
                            val adapterComparingPrice = ComparingPriceAdapter(comparingPriceData)
                            recyclerViewComparingPrice.adapter = adapterComparingPrice
                        }
                    }
                }

                // 데이터를 RecyclerView에 설정
                val adapterComparingPrice = ComparingPriceAdapter(comparingPriceData)
                recyclerViewComparingPrice.adapter = adapterComparingPrice

            }
            .addOnFailureListener { exception ->
                // 데이터 가져오기 실패 시 처리
                Log.e("FirestoreError", "Error getting documents: ", exception)
            }

        val seeingMoreTextView = findViewById<TextView>(R.id.seeingMore)
        seeingMoreTextView.setOnClickListener {
            val intent = Intent(this, ComparingPriceListActivity::class.java)
            startActivity(intent)
        }

        var bnv_main = findViewById(R.id.bnv_main) as BottomNavigationView

        // OnNavigationItemSelectedListener를 통해 탭 아이템 선택 시 이벤트를 처리
        // navi_menu.xml 에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.
        bnv_main.run { setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.first -> {
                    // 다른 액티비티로 이동
                    val intent = Intent(this@MainActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.second -> {
                    // 다른 액티비티로 이동
                    val intent = Intent(this@MainActivity, ReviewActivity::class.java)
                    startActivity(intent)
                }
                R.id.third -> {
                    // 다른 액티비티로 이동
                    val intent = Intent(this@MainActivity, ComparingPriceListActivity::class.java)
                    startActivity(intent)
                }
                R.id.four -> {
                    // 다른 액티비티로 이동
                    val intent = Intent(this@MainActivity, MypageActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }}

    }

}