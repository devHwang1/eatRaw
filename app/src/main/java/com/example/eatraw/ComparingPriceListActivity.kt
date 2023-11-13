package com.example.eatraw

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatraw.adapter.ComparingPriceAdapter
import com.example.eatraw.data.ComparingPriceItem
import com.example.eatraw.databinding.ActivityComparingPriceListBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class ComparingPriceListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComparingPriceListBinding
    private lateinit var recyclerViewComparingPrice: RecyclerView
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComparingPriceListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerViewComparingPrice = binding.recyclerViewComparingPrice
        recyclerViewComparingPrice.layoutManager = LinearLayoutManager(this)

        // Firestore에서 데이터 가져오고 정렬하기
        firestore.collection("fish")
            .orderBy("f_season")
            .get()
            .addOnSuccessListener { documents ->
                val comparingPriceData = mutableListOf<ComparingPriceItem>()

                for (document in documents) {
                    val fishName = document.getString("f_name")
                    val count = (document["f_count"] as? Long)?.toInt() ?: 0
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


        val searchView = findViewById<androidx.appcompat.widget.SearchView>(R.id.mainSearchbar)
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 사용자가 검색 버튼을 눌렀을 때의 동작
                query?.let { searchFish(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 검색어가 변경될 때의 동작
                newText?.let { searchFish(it) }
                return true
            }
        })


        // 뒤로가기 버튼 처리
        val backButton = binding.imgBackarrow
        backButton.setOnClickListener {
            onBackPressed()
        }

        var bnv_main = findViewById(R.id.bnv_main) as BottomNavigationView

        // OnNavigationItemSelectedListener를 통해 탭 아이템 선택 시 이벤트를 처리
        // navi_menu.xml 에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.
        bnv_main.run { setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.first -> {
                    // 다른 액티비티로 이동
                    val intent = Intent(this@ComparingPriceListActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.second -> {
                    // 다른 액티비티로 이동
                    val intent = Intent(this@ComparingPriceListActivity, ReviewActivity::class.java)
                    startActivity(intent)
                }
                R.id.third -> {
                    // 다른 액티비티로 이동
                    val intent = Intent(this@ComparingPriceListActivity, ComparingPriceListActivity::class.java)
                    startActivity(intent)
                }
                R.id.four -> {
                    // 다른 액티비티로 이동
                    val intent = Intent(this@ComparingPriceListActivity, MypageActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }}
    }

    private fun searchFish(query: String) {
        // Firestore에서 검색어를 기반으로 데이터 가져오기
        val uppercaseQuery = query.toUpperCase() // 대소문자 구분 없이 검색하기 위해 대문자로 변환

        firestore.collection("fish")
            .orderBy("f_name")
            .startAt(uppercaseQuery)
            .endAt(uppercaseQuery + "\uf8ff")
            .get()
            .addOnSuccessListener { documents ->
                val comparingPriceData = mutableListOf<ComparingPriceItem>()

                for (document in documents) {
                    val fishName = document.getString("f_name")
                    val count = document.getLong("f_count")?.toInt() ?: 0
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
                    }
                }

                // RecyclerView에 업데이트
                val adapterComparingPrice = ComparingPriceAdapter(comparingPriceData)
                recyclerViewComparingPrice.adapter = adapterComparingPrice
            }
            .addOnFailureListener { exception ->
                // 데이터 가져오기 실패 시 처리
                Log.e("FirestoreError", "Error getting documents: ", exception)
            }
    }

}