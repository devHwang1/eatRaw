package com.example.eatraw

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eatraw.adapter.ReviewAdapter
import com.example.eatraw.data.Review
import com.example.eatraw.databinding.ActivityReviewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ReviewActivity : AppCompatActivity() {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var binding: ActivityReviewBinding
    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReviewAdapter
    private val db = FirebaseFirestore.getInstance()
    private val itemList = arrayListOf<Review>()
    private lateinit var searchView : SearchView
    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        var isSpinner1FirstSelection = true
        var isSpinner2FirstSelection = true
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            // 새로 고침 작업을 수행
            loadAllReviews()

            // 새로 고침 완료 시
            swipeRefreshLayout.isRefreshing = false
        }

        spinner1 = binding.spinner1
        spinner2 = binding.spinner2
        recyclerView = binding.reviewRecycler
        searchView = binding.searchView

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ReviewAdapter(itemList)
        recyclerView.adapter = adapter

        val region = ArrayList<String>()
        region.add("지역")
        val spinner1Adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, region)
        spinner1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = spinner1Adapter

        spinner1.setSelection(0, false)

        var markets = ArrayList<String>()
        val spinner2Adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, markets)
        spinner2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = spinner2Adapter



        markets.add("선택하세요")
        spinner2Adapter.notifyDataSetChanged()
        loadAllReviews()

        db.collection("region")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val regionName = document.id
                    region.add(regionName)
                }
                spinner1Adapter.notifyDataSetChanged()
                Log.w("REviewActivity성공@@@@@@@^#^#^#^", "에러내용: 성공이요")
            }
            .addOnFailureListener { exception ->
                Log.w("REviewActivity에러@@@@@@", "에러내용: $exception")
            }

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedRegion = region[position]
                updateSpinner2(selectedRegion)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            private fun updateSpinner2(selectedRegion: String) {
                db.collection("region").document(selectedRegion)
                    .get()
                    .addOnSuccessListener { document ->
                        val marketList = document.get("markets") as List<String>
                        markets.clear()
                        markets.add("선택하세요")
                        markets.addAll(marketList)
                        (spinner2.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                    }
                    .addOnFailureListener { exception ->
                        Log.w("@@@@@@@@@@@@@@@@@씰패", "실패: $exception")
                    }
            }
        }

        // Spinner2에서 market 선택 시 해당 market의 리뷰만 가져오기
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isSpinner2FirstSelection) {
                    isSpinner2FirstSelection = false
                } else {
                    val selectedMarket = markets[position]
                    loadReviewsForMarket(selectedMarket)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            private fun loadReviewsForMarket(selectedMarket: String) {
                db.collection("review")
                    .whereEqualTo("marketName", selectedMarket)
                    .get()
                    .addOnSuccessListener { result ->
                        val newItems = mutableListOf<Review>()
                        if (result.size() > 0) {
                            for (document in result) {
                                val content = document["content"] as String
                                val marketName = document["marketName"] as String
                                val storeName = document["storeName"] as String
                                val rating = document["rating"]?.toString()?.toDoubleOrNull()
                                val storeImg = document["storeImg"] as String?
                                val region = document["region"] as String?
                                val like = (document["like"] as? Long)?.toInt() // "like" 필드를 Int로 가져오기
                                val cost = (document["cost"] as? Long)?.toInt()

                                val fishKind = document["fishKind"] as String?
                                val userId = document["userId"] as String?


                                val storageReference = FirebaseStorage.getInstance().reference
                                val imageRef = storageReference.child("storeImg/$storeImg")

                                imageRef.downloadUrl.addOnSuccessListener { uri ->
                                    val imageUrl = uri.toString()
                                    val marketNameWithHash = "#$marketName"
                                    val item = Review(content, marketNameWithHash, imageUrl, storeName, rating, region,like,cost,fishKind,userId)
                                    newItems.add(item)
                                    Log.w("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@result.size()", "$result.size()")
                                    Log.w("%#######################", "들오엄")
                                    itemList.clear()
                                    itemList.addAll(newItems)
                                    adapter.notifyDataSetChanged()
                                }
                            }
                        } else {
                            Log.w("%#&&&&&&&&&&&&&&&&&&&&&&&", "들오엄2")
                            showToast("해당 시장에는 리뷰가 없습니다.")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("ReviewActivity", "Error: $exception")
                    }
            }
        }

        // SearchView에 대한 Query Text 변경 리스너 설정
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 사용자가 검색 버튼을 누르면 호출됩니다.
                // Firestore에서 query를 사용하여 검색 작업을 수행하세요.
                performSearch(query)
                performSearch2(query)
                performSearch3(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // SearchView의 텍스트가 변경될 때마다 호출됩니다.
                // Firestore에서 newText를 사용하여 실시간 검색 또는 자동완성을 구현할 수 있습니다.
                return true
            }
        })
        var bnv_main = findViewById(R.id.bnv_main) as BottomNavigationView

        // OnNavigationItemSelectedListener를 통해 탭 아이템 선택 시 이벤트를 처리
        // navi_menu.xml 에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.
        bnv_main.run { setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.first -> {
                    // 다른 액티비티로 이동
                    val intent = Intent(this@ReviewActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.second -> {
                    // 다른 액티비티로 이동
                    val intent = Intent(this@ReviewActivity, ReviewActivity::class.java)
                    startActivity(intent)
                }
                R.id.third -> {
                    // 다른 액티비티로 이동
                    val intent = Intent(this@ReviewActivity, ComparingPriceListActivity::class.java)
                    startActivity(intent)
                }
                R.id.four -> {
                    // 다른 액티비티로 이동
                    val intent = Intent(this@ReviewActivity, ComparingPriceListActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }}
        var floatingActionButton = findViewById(R.id.floatingActionButton) as FloatingActionButton
        floatingActionButton.setOnClickListener{
            val intent = Intent(this@ReviewActivity,WriteActivity::class.java)
            startActivity(intent)
        }
    }



    private fun loadAllReviews() {
        db.collection("review")
            .get()
            .addOnSuccessListener { result ->
                val newItems = mutableListOf<Review>()
                for (document in result) {
                    val content = document["content"] as String
                    val marketName = document["marketName"] as String
                    val storeName = document["storeName"] as String
                    val rating = document["rating"]?.toString()?.toDoubleOrNull() // rating을 Float로 가져옴
                    val storeImg = document["storeImg"] as String?
                    val region = document["region"] as String?
                    val like = (document["like"] as? Long)?.toInt() // "like" 필드를 Int로 가져오기
                    val cost = (document["cost"] as? Long)?.toInt()
                    val fishKind = document["fishKind"] as String?
                    val userId = document["userId"] as String?

                    // 이미지 URL이 없으면 기본 이미지 URL로 대체
                    val imageUrl = storeImg ?: "기본 이미지 URL" // 여기에 기본 이미지 URL을 넣으세요

                    val marketNameWithHash = "#$marketName"
                    val item = Review(content, marketNameWithHash, imageUrl, storeName, rating, region, like, cost, fishKind,userId)
                    newItems.add(item)
                }

                itemList.clear()
                itemList.addAll(newItems)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("ReviewActivity", "Error: $exception")
            }
    }

    private fun performSearch(query: String?) {
        if (query.isNullOrBlank()) {
            // 검색어가 비어있거나 공백인 경우, 모든 리뷰를 표시
            loadAllReviews()
        } else {
            // Firestore에서 검색어를 이용하여 데이터 검색
            db.collection("review")
                .whereEqualTo("marketName", query) // marketName 필드에서 검색어와 일치하는 항목을 찾습니다.
                .get()
                .addOnSuccessListener { result ->
                    val newItems = mutableListOf<Review>()
                    for (document in result) {
                        val content = document["content"] as String
                        val marketName = document["marketName"] as String
                        val storeName = document["storeName"] as String
                        val rating = document["rating"]?.toString()?.toDoubleOrNull()
                        val storeImg = document["storeImg"] as String?
                        val region = document["region"] as String?
                        val like = (document["like"] as? Long)?.toInt() // "like" 필드를 Int로 가져오기
                        val cost = (document["cost"] as? Long)?.toInt()
                        val fishKind = document["fishKind"] as String?
                        val userId = document["userId"] as String?
                        val storageReference = FirebaseStorage.getInstance().reference
                        val imageRef = storageReference.child("storeImg/$storeImg")

                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()
                            val marketNameWithHash = "#$marketName"
                            val item = Review(content, marketNameWithHash, imageUrl, storeName, rating, region,like,cost,fishKind,userId)
                            newItems.add(item)
                            itemList.clear()
                            itemList.addAll(newItems)
                            adapter.notifyDataSetChanged()

                            if (newItems.isEmpty()) {
                                // 검색 결과가 없을 때 사용자에게 메시지를 표시합니다.
                                showToast("검색 결과가 없습니다.")
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("ReviewActivity", "Error: $exception")
                }
        }
    }
    private fun performSearch2(query: String?) {
        if (query.isNullOrBlank()) {
            // 검색어가 비어있거나 공백인 경우, 모든 리뷰를 표시
            loadAllReviews()
        } else {
            // Firestore에서 검색어를 이용하여 데이터 검색
            db.collection("review")
                .whereEqualTo("storeName", query) // marketName 필드에서 검색어와 일치하는 항목을 찾습니다.
                .get()
                .addOnSuccessListener { result ->
                    val newItems = mutableListOf<Review>()
                    for (document in result) {
                        val content = document["content"] as String
                        val marketName = document["marketName"] as String
                        val storeName = document["storeName"] as String
                        val rating = document["rating"]?.toString()?.toDoubleOrNull()
                        val storeImg = document["storeImg"] as String?
                        val region = document["region"] as String?
                        val like = (document["like"] as? Long)?.toInt() // "like" 필드를 Int로 가져오기
                        val cost = (document["cost"] as? Long)?.toInt()
                        val fishKind = document["fishKind"] as String?
                        val userId = document["userId"] as String?
                        val storageReference = FirebaseStorage.getInstance().reference
                        val imageRef = storageReference.child("storeImg/$storeImg")

                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()
                            val marketNameWithHash = "#$marketName"
                            val item = Review(content, marketNameWithHash, imageUrl, storeName, rating, region,like,cost, fishKind,userId)
                            newItems.add(item)
                            itemList.clear()
                            itemList.addAll(newItems)
                            adapter.notifyDataSetChanged()

                            if (newItems.isEmpty()) {
                                // 검색 결과가 없을 때 사용자에게 메시지를 표시합니다.
                                showToast("검색 결과가 없습니다.")
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("ReviewActivity", "Error: $exception")
                }
        }
    }
    private fun performSearch3(query: String?) {
        if (query.isNullOrBlank()) {
            // 검색어가 비어있거나 공백인 경우, 모든 리뷰를 표시
            loadAllReviews()
        } else {
            // Firestore에서 검색어를 이용하여 데이터 검색
            db.collection("review")
                .whereEqualTo("content", query) // marketName 필드에서 검색어와 일치하는 항목을 찾습니다.
                .get()
                .addOnSuccessListener { result ->
                    val newItems = mutableListOf<Review>()
                    for (document in result) {
                        val content = document["content"] as String
                        val marketName = document["marketName"] as String
                        val storeName = document["storeName"] as String
                        val rating = document["rating"]?.toString()?.toDoubleOrNull()
                        val storeImg = document["storeImg"] as String?
                        val region = document["region"] as String?
                        val like = (document["like"] as? Long)?.toInt() // "like" 필드를 Int로 가져오기
                        val cost = (document["cost"] as? Long)?.toInt()
                        val fishKind = document["fishKind"] as String?
                        val userId = document["userId"] as String?
                        val storageReference = FirebaseStorage.getInstance().reference
                        val imageRef = storageReference.child("storeImg/$storeImg")

                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()
                            val marketNameWithHash = "#$marketName"
                            val item = Review(content, marketNameWithHash, imageUrl, storeName, rating, region,like, cost, fishKind,userId)
                            newItems.add(item)
                            itemList.clear()
                            itemList.addAll(newItems)
                            adapter.notifyDataSetChanged()

                            if (newItems.isEmpty()) {
                                // 검색 결과가 없을 때 사용자에게 메시지를 표시합니다.
                                showToast("검색 결과가 없습니다.")
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("ReviewActivity", "Error: $exception")
                }
        }
    }

}
