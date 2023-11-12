package com.example.eatraw

import android.content.Intent
import android.graphics.Color
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.eatraw.adapter.BannerFragmentAdapter
import com.example.eatraw.adapter.BestReviewAdapter
import com.example.eatraw.adapter.ComparingPriceAdapter
import com.example.eatraw.data.ComparingPriceItem
import com.example.eatraw.data.Review
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

    private lateinit var viewPager2: ViewPager2

    // Firebase Firestore 인스턴스 가져오기
    private val firestore = FirebaseFirestore.getInstance()


    private val bestReviewData: MutableList<Review> = mutableListOf()

    private val comparingPriceData: List<ComparingPriceItem> = listOf(

        // 추가적인 ComparingPriceItem 인스턴스와 생선 이름, 가격을 추가하세요.
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        barChart = findViewById(R.id.barChart)
        viewPager2 = findViewById(R.id.view_pager2_banner)
        textViewq = findViewById(R.id.textViewq)
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
            val intent = Intent(applicationContext, StartActivity::class.java)
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

    fun changeColor() {
        when(viewPager2.currentItem){
            0 ->
            {
                iv1.setBackgroundColor(applicationContext.resources.getColor(R.color.blue))
                iv2.setBackgroundColor(applicationContext.resources.getColor(R.color.opgray))
                iv3.setBackgroundColor(applicationContext.resources.getColor(R.color.opgray))
            }
            1 ->
            {
                iv1.setBackgroundColor(applicationContext.resources.getColor(R.color.opgray))
                iv2.setBackgroundColor(applicationContext.resources.getColor(R.color.blue))
                iv3.setBackgroundColor(applicationContext.resources.getColor(R.color.opgray))
            }
            2 ->
            {
                iv1.setBackgroundColor(applicationContext.resources.getColor(R.color.opgray))
                iv2.setBackgroundColor(applicationContext.resources.getColor(R.color.opgray))
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

        // Assuming you have a helper function to set an image by URL or resource ID
        setFishImage(randomFishImageView, randomFishData.fishImg)
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

    private fun loadAndDisplayFishKindAveragesForMarket(marketName: String, startTimestamp: Timestamp, endTimestamp: Timestamp) {
        val firestore = FirebaseFirestore.getInstance()
        val fishKindAverages = mutableMapOf<String, Float>()
        Log.w("시간값알기", "$startTimestamp")
        Log.w("시간값알기", "$endTimestamp")
        Log.w("시간값알기", "$marketName")
        textViewq.text = "$marketName" + " 어종별 시세"
        for (fishKind in fishKinds) {
            firestore.collection("review")
                .whereEqualTo("marketName", marketName)
                .whereEqualTo("fishKind", fishKind)
                .whereGreaterThanOrEqualTo("timestamp", startTimestamp)
                .whereLessThanOrEqualTo("timestamp", endTimestamp)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    var totalCost = 0.0
                    var count = 0

                    for (document in querySnapshot) {
                        val cost = document.getDouble("cost")
                        if (cost != null) {
                            totalCost += cost
                            count++
                        }
                    }

                    if (count > 0) {
                        val averageCost = totalCost.toFloat() / count
                        fishKindAverages[fishKind] = averageCost
                        Log.w("평균값알기", "$averageCost")
                        Log.w("평균값알기", "$totalCost")
                        Log.w("평균값알기", "$fishKindAverages")
                    }

                    if (fishKindAverages.size == fishKinds.size) {
                        displayFishKindAveragesChart(fishKindAverages)
                    }
                }
                .addOnFailureListener { exception ->
                }
        }
    }

    private fun displayFishKindAveragesChart(fishKindAverages: Map<String, Float>) {
        val barEntries = mutableListOf<BarEntry>()
        val labels = mutableListOf<String>()

        for ((index, fishKind) in fishKinds.withIndex()) {
            val averageCost = fishKindAverages[fishKind] ?: 0.0f
            barEntries.add(BarEntry(index.toFloat(), averageCost))
            labels.add(fishKind)
        }

        val colors = intArrayOf(
            Color.rgb(197,255,140), Color.rgb(255,247,139)
                + Color.rgb(255, 211, 140), Color.rgb(140, 235, 255), Color.rgb(255, 142, 155)) // 막대의 갯수에 따라서 원하는 색상을 추가하세요

        val barDataSet = BarDataSet(barEntries, "어종별 평균 시세")
        barDataSet.setColors(*colors)

        val dataSets: ArrayList<IBarDataSet> = ArrayList()
        dataSets.add(barDataSet)

        val data = BarData(dataSets)
        data.setValueTextSize(15f)
        data.barWidth = 0.4f

        barChart.data = data
        barChart.description.isEnabled = false
        barChart.setFitBars(true)
        barChart.animateY(1000)
        barChart.setDrawGridBackground(false)

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setGranularity(1f)
        xAxis.textSize = 12f

        barChart.invalidate()
    }
    // 최신순으로 9개의 리뷰를 가져오는 함수
//    private fun fetchLatestReviews() {
//        // Firestore에서 review 컬렉션을 최신순으로 정렬하여 상위 9개를 가져옵니다.
//        firestore.collection("review")
//            .orderBy("timestamp", Query.Direction.DESCENDING)
//            .limit(9)
//            .get()
//            .addOnSuccessListener { documents ->
//                val latestReviewsData = mutableListOf<Review>()
//
//                for (document in documents) {
//                    val content = document.getString("content")
//                    val marketName = document.getString("marketName")
//                    val storeImg = document.getString("storeImg")
//                    val storeName = document.getString("storeName")
//                    val rating = document.getDouble("rating")
//                    val region = document.getString("region")
//                    val like = document.getLong("like")?.toInt()
//                    val fishKind = document.getString("fishKind")
//                    val cost = document.getLong("cost")?.toInt()
//                    val userId = document.getString("userId")
//                    val reviewId = document.getString("reviewId")
//                    val timestamp = document.getTimestamp("timestamp")
//
//                    if (content != null && marketName != null && storeName != null && rating != null
//                        && userId != null && reviewId != null && timestamp != null) {
//                        // 데이터를 RecyclerView에 설정할 데이터 클래스에 추가합니다.
//                        val review = Review(
//                            content, marketName, storeImg, storeName, rating, region,
//                            like, fishKind, cost, userId, reviewId, timestamp
//                        )
//                        latestReviewsData.add(review)
//                    }
//                }
//
//                // RecyclerView 어댑터에 데이터를 설정합니다.
//                val adapterLatestReviews = ReviewAdapter(latestReviewsData)
//                mainReviewRecycler.adapter = adapterLatestReviews
//            }
//            .addOnFailureListener { exception ->
//                // 데이터를 가져오지 못한 경우 처리
//                Log.e("FirestoreError", "최신 리뷰 가져오기 오류: ", exception)
//            }
//    }



}