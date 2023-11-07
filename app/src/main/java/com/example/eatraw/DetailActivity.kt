package com.example.eatraw

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatraw.adapter.ReviewDetailAdapter
import com.example.eatraw.data.DetailReview
import com.example.eatraw.data.Review
import com.example.eatraw.databinding.ActivityDetailBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : ReviewDetailAdapter
    private val itemList = arrayListOf<Review>()
    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)

        recyclerView = binding.detailRecylerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ReviewDetailAdapter(itemList)
        recyclerView.adapter = adapter


        db.collection("review")
            .get()
            .addOnSuccessListener { result ->
                val newItems = mutableListOf<Review>()
                for (document in result){
                    val content = document["content"] as String
                    val marketName = document["marketName"] as String
                    val storeName = document["storeName"] as String
<<<<<<< HEAD
                    val rating = document["rating"] as Double?
=======
                    val rating = document["rating"]?.toString()?.toDoubleOrNull()
>>>>>>> e83978d81e46812c888fafade304e3cc29213a84
                    val storeImg = document["storeImg"] as String?
                    val region = document["region"] as String?
                    val like = (document["like"] as? Long)?.toInt()
                    val cost = (document["cost"] as? Long)?.toInt()
                    val fishKind = document["fishKind"] as String?
<<<<<<< HEAD
=======
                    val userId = document["userId"] as String?
>>>>>>> e83978d81e46812c888fafade304e3cc29213a84
                    val storageReference =  FirebaseStorage.getInstance().reference
                    val imageRef = storageReference.child("storeImg/$storeImg")

                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        val marketNameWithHash = "#$marketName"
<<<<<<< HEAD
                        val item = Review(content, marketNameWithHash, imageUrl, storeName, rating, region,like,cost,fishKind)
=======
                        val item = Review(content, marketNameWithHash, imageUrl, storeName, rating, region,like,cost,fishKind,userId)
>>>>>>> e83978d81e46812c888fafade304e3cc29213a84
                        newItems.add(item)

                        itemList.clear()
                        itemList.addAll(newItems)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("DetailActivity", "Error: $exception")
            }
    }

}