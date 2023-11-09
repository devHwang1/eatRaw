package com.example.eatraw

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.eatraw.adapter.BannerFragmentAdapter
import com.example.eatraw.adapter.BestReviewAdapter
import com.example.eatraw.adapter.ComparingPriceAdapter
import com.example.eatraw.data.BestReviewItem
import com.example.eatraw.data.ComparingPriceItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.Timer
import java.util.TimerTask


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var button: Button
    private lateinit var textView: TextView
    private var user: FirebaseUser? = null

//    private lateinit var recyclerViewBanner: RecyclerView
    private lateinit var recyclerViewBestReview: RecyclerView
    private lateinit var recyclerViewComparingPrice: RecyclerView

    private lateinit var iv1: ImageView
    private lateinit var iv2: ImageView
    private lateinit var iv3: ImageView

    private lateinit var viewPager2: ViewPager2

    // Firebase Firestore 인스턴스 가져오기
    private val firestore = FirebaseFirestore.getInstance()


    private val bestReviewData: MutableList<BestReviewItem> = mutableListOf()

    private val comparingPriceData: List<ComparingPriceItem> = listOf(

        // 추가적인 ComparingPriceItem 인스턴스와 생선 이름, 가격을 추가하세요.
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager2 = findViewById(R.id.view_pager2_banner)
        iv1 = findViewById(R.id.iv1)
        iv2 = findViewById(R.id.iv2)
        iv3 = findViewById(R.id.iv3)

        val images = listOf(R.drawable.banner1, R.drawable.banner2, R.drawable.banner3)
        val adapterViewPager2Banner = BannerFragmentAdapter(this, images)
        viewPager2.adapter = adapterViewPager2Banner

        val handler = Handler(Looper.getMainLooper())
        val timer = Timer()

        // 일정 시간 간격으로 페이지를 변경
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    val nextItem = (viewPager2.currentItem + 1) % images.size
                    viewPager2.currentItem = nextItem
                }
            }
        }, 5000, 5000) // 5000ms(5초) 간격으로 페이지 변경

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                changeColor()
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                changeColor()
            }
        })

        iv1.setOnClickListener {
            viewPager2.currentItem = 0 // 첫 번째 페이지로 이동
        }

        iv2.setOnClickListener {
            viewPager2.currentItem = 1 // 두 번째 페이지로 이동
        }

        iv3.setOnClickListener {
            viewPager2.currentItem = 2 // 세 번째 페이지로 이동
        }


        // RecyclerView 초기화
        recyclerViewBestReview = findViewById(R.id.recyclerViewBestReview)
        recyclerViewComparingPrice = findViewById(R.id.recyclerViewComparingPrice)

        // 레이아웃 매니저 설정
        val layoutManagerBestReview =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerComparingPrice =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recyclerViewBestReview.layoutManager = layoutManagerBestReview
        recyclerViewComparingPrice.layoutManager = layoutManagerComparingPrice

        // 어댑터 설정
        val adapterBestReview = BestReviewAdapter(bestReviewData)
        val adapterComparingPrice = ComparingPriceAdapter(comparingPriceData)

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


        // Firestore에서 데이터 가져오고 정렬하기
        firestore.collection("fish")
            .orderBy("f_season")
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
//                    val storageReference = FirebaseStorage.getInstance().reference
//                    val imageRef = storageReference.child("FishImg/$fishImg")

                    if (fishName != null && minCost != null && avgCost != null && maxCost != null) {
//                        imageRef.downloadUrl.addOnSuccessListener { uri ->
//                            val imageUrl = uri.toString()
                        val comparingPriceItem = ComparingPriceItem(
                            fishName,
                            minCost.toString(),
                            avgCost.toString(),
                            maxCost.toString(),
                            fishImg,
                            season
                        )
                        comparingPriceData.add(comparingPriceItem)

                        // 데이터를 RecyclerView에 설정
                        val adapterComparingPrice = ComparingPriceAdapter(comparingPriceData)
                        recyclerViewComparingPrice.adapter = adapterComparingPrice
//                        }
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

    fun changeColor() {
        when(viewPager2.currentItem){
            0 ->
            {
                iv1.setBackgroundColor(applicationContext.resources.getColor(R.color.black))
                iv2.setBackgroundColor(applicationContext.resources.getColor(R.color.gray))
                iv3.setBackgroundColor(applicationContext.resources.getColor(R.color.gray))
            }
            1 ->
            {
                iv1.setBackgroundColor(applicationContext.resources.getColor(R.color.gray))
                iv2.setBackgroundColor(applicationContext.resources.getColor(R.color.black))
                iv3.setBackgroundColor(applicationContext.resources.getColor(R.color.gray))
            }
            2 ->
            {
                iv1.setBackgroundColor(applicationContext.resources.getColor(R.color.gray))
                iv2.setBackgroundColor(applicationContext.resources.getColor(R.color.gray))
                iv3.setBackgroundColor(applicationContext.resources.getColor(R.color.black))
            }
        }
    }

}