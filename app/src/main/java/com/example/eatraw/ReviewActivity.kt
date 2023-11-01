package com.example.eatraw

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatraw.adapter.ReviewAdapter
import com.example.eatraw.data.Review
import com.example.eatraw.databinding.ActivityReviewBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewBinding
    private lateinit var spinner: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReviewAdapter
    val db = FirebaseFirestore.getInstance()
    val itemList = arrayListOf<Review>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinner = binding.spinner1
        spinner2 = binding.spinner2
        recyclerView = binding.reviewRecycler

        // RecyclerView의 레이아웃 매니저 설정
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 어댑터 초기화
        adapter = ReviewAdapter(itemList)

        // RecyclerView에 어댑터 설정
        recyclerView.adapter = adapter
        // Firestore에서 Review 객체를 가져오는 부분
        db.collection("review")
            .get()
            .addOnSuccessListener { result ->
                val newItems = mutableListOf<Review>()

                for (document in result) {
                    val content = document["content"] as String
                    val marketName = document["marketName"] as String
                    val storeName = document["storeName"] as String
                    val rating = document["rating"] as Double?
                    val storeImg = document["storeImg"] as String?

                    // 이미지 URL 가져오는 부분
                    val storageReference = FirebaseStorage.getInstance().reference
                    val imageRef = storageReference.child("storeImg/$storeImg")

                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()

                        // marketName 앞에 '#'를 추가
                        val marketNameWithHash = "#$marketName"

                        val item = Review(content, marketNameWithHash, imageUrl, storeName, rating)
                        newItems.add(item)
                        if (newItems.size == result.size()) {
                            itemList.addAll(newItems)
                            adapter.notifyItemRangeInserted(itemList.size - newItems.size, newItems.size)
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("ReviewActivity", "Error: $exception")
            }


    }
}