package com.example.eatraw

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.eatraw.adapter.BannerFragmentAdapter
import com.example.eatraw.adapter.BestReviewAdapter
import com.example.eatraw.adapter.ComparingPriceAdapter
import com.example.eatraw.data.ComparingPriceItem
import com.example.eatraw.data.Review
import com.example.eatraw.mypagefrg.ModifyFragment
import com.example.eatraw.mypagefrg.PasswordFragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.Timer
import java.util.TimerTask


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var liveCost : LinearLayout
    private lateinit var button: Button
    private lateinit var textView: TextView
    private var user: FirebaseUser? = null
    private lateinit var randomFishData: ComparingPriceItem
    private lateinit var barChart: BarChart
    private lateinit var textViewq: TextView
//    private lateinit var recyclerViewBanner: RecyclerView
    private lateinit var recyclerViewBestReview: RecyclerView
    private lateinit var recyclerViewComparingPrice: RecyclerView
//    private lateinit var mainReviewRecycler: RecyclerView
    private val fishKinds = listOf( "우럭", "참돔", "방어", "전복", "돌돔")
    private lateinit var iv1: ImageView
    private lateinit var iv2: ImageView
    private lateinit var iv3: ImageView
    private lateinit var dehaze: ImageView
    private lateinit var viewPager2: ViewPager2

    // Firebase Firestore 인스턴스 가져오기
    private val firestore = FirebaseFirestore.getInstance()


    private val bestReviewData: MutableList<Review> = mutableListOf()

    private val comparingPriceData: List<ComparingPriceItem> = listOf(

        // 추가적인 ComparingPriceItem 인스턴스와 생선 이름, 가격을 추가하세요.
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        liveCost = findViewById(R.id.LiveCost)
        barChart = findViewById(R.id.barChart)
        viewPager2 = findViewById(R.id.view_pager2_banner)
        textViewq = findViewById(R.id.textViewq)
        iv1 = findViewById(R.id.iv1)
        iv2 = findViewById(R.id.iv2)
        iv3 = findViewById(R.id.iv3)
        dehaze = findViewById(R.id.dehaze)
        val images = listOf(R.drawable.banner1, R.drawable.banner2, R.drawable.banner3)
        val adapterViewPager2Banner = BannerFragmentAdapter(this, images)
        viewPager2.adapter = adapterViewPager2Banner

        val handler = Handler(Looper.getMainLooper())
        val timer = Timer()
        val db  = FirebaseFirestore.getInstance()

        liveCost.setOnClickListener {
            // Firebase Authentication을 사용한다고 가정
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                val userId = user.uid
                Log.d("유저아이디에 대해알아봅시다", "$userId")
                getUserInfo(userId)
            } else {
                Log.d("유저아이디에 대해알아봅시다", "안되는중입니다")
            }
        }

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
//        mainReviewRecycler = findViewById(R.id.mainReviewRecycler)

        // 레이아웃 매니저 설정
        val layoutManagerBestReview =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerComparingPrice =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        val layoutManagerMainReview =
//            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recyclerViewBestReview.layoutManager = layoutManagerBestReview
        recyclerViewComparingPrice.layoutManager = layoutManagerComparingPrice
//        mainReviewRecycler.layoutManager = layoutManagerMainReview

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
            .addOnSuccessListener { result ->
                val newItems = mutableListOf<Review>()
                for (document in result) {
                    val content = document["content"] as String
                    val marketName = document["marketName"] as String
                    val storeName = document["storeName"] as String
                    val rating = document["rating"]?.toString()?.toDoubleOrNull() // rating을 Double로 가져옴
                    val storeImg = document["storeImg"] as String?
                    val region = document["region"] as String?
                    val like = (document["like"] as? Long)?.toInt() // "like" 필드를 Int로 가져오기
                    val cost = (document["cost"] as? Long)?.toInt()
                    val fishKind = document["fishKind"] as String?
                    val userId = document["userId"] as String?

                    // 이미지 URL이 없으면 기본 이미지 URL로 대체
                    val imageUrl = storeImg ?: "기본 이미지 URL" // 여기에 기본 이미지 URL을 넣으세요

                    val marketNameWithHash = "$marketName"
                    val item = Review(content, marketNameWithHash, imageUrl, storeName, rating, region, like, fishKind, cost, userId)
                    newItems.add(item)
                }

                bestReviewData.clear()
                bestReviewData.addAll(newItems)
                adapterBestReview.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("ReviewActivity", "Error: $exception")
            }


        // Firestore에서 데이터 가져오고 정렬하기
        firestore.collection("fish")
            .orderBy("f_season")
            .get()
            .addOnSuccessListener { documents ->
                val comparingPriceData = mutableListOf<ComparingPriceItem>()

                for (document in documents) {
                    val fishName = document.getString("f_name")
                    val count = document.getLong("f_count")?.toInt()
                    val minCost = (document["f_min"] as? Long)?.toInt()
                    val avgCost = (document["f_avg"] as? Long)?.toInt()
                    val maxCost = (document["f_max"] as? Long)?.toInt()
                    val fishImg = document.getString("f_img")
                    val season = document.getString("f_season")
//                    val storageReference = FirebaseStorage.getInstance().reference
//                    val imageRef = storageReference.child("FishImg/$fishImg")

                    if (fishName != null && count != null && minCost != null && avgCost != null && maxCost != null) {
                        val comparingPriceItem = ComparingPriceItem(
                            fishName,
                            count,
                            minCost.toLong(),
                            avgCost.toLong(),
                            maxCost.toLong(),
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



        dehaze.setOnClickListener {
            val intent = Intent(this, MypageActivity::class.java)
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


        // 추천 어종
        fetchRandomFishData()

        // 최신 리뷰
//        fetchLatestReviews()

    }

    val db  = FirebaseFirestore.getInstance()
    fun getUserInfo(userId: String) {
        val docRef = db.collection("users").document(userId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val likeMarket = document.getString("likeMarket")
                    if (likeMarket != null) {
                        // likeMarket이 null이 아니면 QuoteActivity로 전달
                        val intent = Intent(this, QuoteActivity::class.java)
                        intent.putExtra("likeMarket", likeMarket)
                        startActivity(intent)
                    } else {
                        println("문서에 'likeMarket' 필드가 없습니다")
                    }
                } else {
                    println("해당 문서가 없습니다")
                }
            }
            .addOnFailureListener { exception ->
                println("문서를 가져오는 중 오류가 발생했습니다: $exception")
            }
    }

    fun changeColor() {
        when(viewPager2.currentItem){
            0 ->
            {
                iv1.setBackgroundColor(applicationContext.resources.getColor(R.color.blue))
                iv2.setBackgroundColor(applicationContext.resources.getColor(R.color.gray))
                iv3.setBackgroundColor(applicationContext.resources.getColor(R.color.gray))
            }
            1 ->
            {
                iv1.setBackgroundColor(applicationContext.resources.getColor(R.color.gray))
                iv2.setBackgroundColor(applicationContext.resources.getColor(R.color.blue))
                iv3.setBackgroundColor(applicationContext.resources.getColor(R.color.gray))
            }
            2 ->
            {
                iv1.setBackgroundColor(applicationContext.resources.getColor(R.color.gray))
                iv2.setBackgroundColor(applicationContext.resources.getColor(R.color.gray))
                iv3.setBackgroundColor(applicationContext.resources.getColor(R.color.blue))
            }
        }
    }


    private fun fetchRandomFishData() {
        firestore.collection("fish")
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // Choose a random fish
                    val randomIndex = (0 until documents.size()).random()
                    val randomDocument = documents.documents[randomIndex]

                    // Extract fish data
                    val fishName = randomDocument.getString("f_name")
                    val count = randomDocument.getLong("f_count")?.toInt() ?: 0
                    val minCost = randomDocument.getLong("f_min")
                    val avgCost = randomDocument.getLong("f_avg")
                    val maxCost = randomDocument.getLong("f_max")
                    val fishImg = randomDocument.getString("f_img")
                    val season = randomDocument.getString("f_season")

                    if (fishName != null && minCost != null && avgCost != null && maxCost != null) {
                        // Update the global variable with the random fish data
                        randomFishData = ComparingPriceItem(fishName, count, minCost, avgCost, maxCost, fishImg, season)
                        updateRandomFishViews()
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle the failure to fetch random fish data
                Log.e("FirestoreError", "Error getting random fish data: ", exception)
            }
    }

    // Add a function to update views with the random fish data
    private fun updateRandomFishViews() {
        // Update the relevant TextViews and ImageView with data from randomFishData
        // For example:
        val randomFishNameTextView: TextView = findViewById(R.id.random_fish_name)
        val randomFishPriceTextView: TextView = findViewById(R.id.random_fish_price)
        val randomFishImageView: ImageView = findViewById(R.id.random_fish_image)

        randomFishNameTextView.text = randomFishData.fishName
        randomFishPriceTextView.text = randomFishData.minCost.toString()

        // URL 또는 리소스 ID를 기반으로 이미지를 설정하는 도우미 함수가 있다고 가정합니다.
        setFishImage(randomFishImageView, randomFishData.fishImg)

        // random_fish_image를 클릭하는 경우를 처리하는 OnClickListener 설정
        randomFishImageView.setOnClickListener {
            // 클릭 이벤트 처리, 예를 들어 ComparingPriceDetailActivity를 시작합니다.
            val intent = Intent(this, ComparingPriceDetailActivity::class.java)
            // intent.putExtra()를 사용하여 ComparingPriceDetailActivity에 필요한 데이터를 전달합니다.
            intent.putExtra("fishImg", randomFishData.fishImg)
            intent.putExtra("fishName", randomFishData.fishName)
            intent.putExtra("minCost", randomFishData.minCost)
            intent.putExtra("avgCost", randomFishData.avgCost)
            intent.putExtra("maxCost", randomFishData.maxCost)
            // ... (다른 전달할 데이터가 있으면 추가하세요)
            startActivity(intent)
        }
    }

    // Hypothetical function to set the fish image
    private fun setFishImage(imageView: ImageView, imageUrl: String?) {
        // Check if imageUrl is not null or empty
        if (!imageUrl.isNullOrEmpty()) {
            // Use Glide to load and display the image
            Glide.with(this)
                .load(imageUrl)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)) // Optional: Cache the image
                .into(imageView)
        } else {
            // Set a default image if imageUrl is null or empty
            imageView.setImageResource(R.drawable.default_nallo)
        }
    }





}