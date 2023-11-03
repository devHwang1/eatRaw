package com.example.eatraw

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatraw.adapter.ComparingPriceAdapter
import com.example.eatraw.data.ComparingPriceItem
import com.example.eatraw.databinding.ActivityComparingPriceListBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage

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

        // 뒤로가기 버튼 처리
        val backButton = binding.imgBackarrow
        backButton.setOnClickListener {
            onBackPressed()
        }
    }
}
