package com.example.eatraw.mypagefrg

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eatraw.adapter.MyReviewAdapter
import com.example.eatraw.data.Review
import com.example.eatraw.data.Users
import com.example.eatraw.databinding.FragmentMyreviewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MyreviewFragment : Fragment() {
    private lateinit var users: List<Users>
    private lateinit var reviews: List<Review>
    private lateinit var binding: FragmentMyreviewBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var searchView: SearchView
    private lateinit var itemList: MutableList<Review>
    private lateinit var adapter: MyReviewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyreviewBinding.inflate(inflater, container, false)
        binding.detailRecylerView.layoutManager = LinearLayoutManager(context)
        searchView = binding.searchView
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.detailRecylerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val noReviewsTextView = binding.noReviewsTextView
        val currentUserId = mAuth.currentUser!!.uid
        var isSearching = false
        itemList = mutableListOf() // itemList 초기화



        lifecycleScope.launch {
            users = getAllUsersFromDb()
            reviews = getAllReviewsFromDb()

            // 현재 로그인한 사용자의 ID를 가져오기
            val currentUserId = mAuth.currentUser!!.uid
            val userReviews = reviews.filter { it.userId == currentUserId }

            adapter = context?.let { MyReviewAdapter(it, itemList, users) }!! // adapter 초기화
            recyclerView.adapter = adapter

            if (userReviews.isEmpty()) {
                noReviewsTextView.visibility = View.VISIBLE
            } else {
                itemList.addAll(userReviews)
                withContext(Dispatchers.Main) {
                    adapter.notifyDataSetChanged()
                }
            }
        }


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                isSearching = true
                itemList.clear()
                performSearch(query)
                performSearch2(query)
                performSearch3(query)

                val distinctItems = itemList.distinctBy { it.reviewId }
                itemList.clear()

                adapter.notifyDataSetChanged()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    isSearching = false
                    loadAllReviews() // 검색창이 비었을 때 원래의 리뷰 목록 로드
                }
                return true
            }
        })
    }

    private suspend fun getAllReviewsFromDb(): List<Review> {
        return withContext(Dispatchers.IO) {
            val reviews = mutableListOf<Review>()
            val docRef = db.collection("review")

            try {
                val documents = docRef.get().await()
                for (document in documents) {
                    val data = document.data // 데이터를 Map으로 가져옵니다.
                    val reviewId = document.id
                    val content = data["content"] as? String ?: ""
                    val marketName = data["marketName"] as? String ?: ""
                    val storeImg = data["storeImg"] as? String ?: ""
                    val storeName = data["storeName"] as? String ?: ""
                    val rating = (data["rating"] as? Double) ?: 0.0
                    val region = data["region"] as? String ?: ""
                    val like = (data["like"] as? Long)?.toInt() ?: 0
                    val fishKind = data["fishKind"] as? String ?: ""
                    val cost = (data["cost"] as? Long)?.toInt() ?: 0
                    val userId = data["userId"] as? String ?: ""

                    val review = Review(
                        content, marketName, storeImg, storeName, rating, region,
                        like, fishKind, cost, userId, reviewId
                    )
                    reviews.add(review)
                }

                // 어댑터 초기화 및 설정

            } catch (exception: Exception) {
                println("Error getting documents: $exception")
            }
            println("Loaded ${reviews.size} reviews")
            return@withContext reviews
        }
    }

    private suspend fun getAllUsersFromDb(): List<Users> {
        return withContext(Dispatchers.IO) {
            val users = mutableListOf<Users>()
            val docRef = db.collection("users")

            try {
                val documents = docRef.get().await()
                for (document in documents) {
                    val email = document.getString("email") ?: ""
                    val nickname = document.getString("nickname") ?: ""
                    val aouthLogin = document.getBoolean("aouthLogin") ?: false
                    val admin = document.getBoolean("admin") ?: false
                    val imageUrl = document.getString("imageUrl") ?: ""
                    val userId = document.getString("userId") ?: ""
                    val likeMarket = document.getString("likeMarket") ?: ""

                    val user = Users(email, nickname, aouthLogin, admin, imageUrl, userId,likeMarket)
                    users.add(user)
                }

                // 어댑터 초기화 및 설정
                println("Loaded ${users.size} users")
            } catch (exception: Exception) {
                println("Error getting documents: $exception")
            }
            return@withContext users
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
                    val cost = (document["cost"] as? Double)?.toInt()
                    val fishKind = document["fishKind"] as String?
                    val userId = document["userId"] as String?


                    // 이미지 URL이 없으면 기본 이미지 URL로 대체
                    val imageUrl = storeImg ?: "기본 이미지 URL" // 여기에 기본 이미지 URL을 넣으세요

                    val marketNameWithHash = "$marketName"
                    val item = Review(content, marketNameWithHash, imageUrl, storeName, rating, region, like, fishKind, cost,userId)
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
            loadAllReviews()
        } else {
            db.collection("review")
                .orderBy("content") // content 필드로 정렬
                .startAt(query) // query로 시작하는 값부터 검색
                .endAt(query + "\uf8ff") // query로 끝나는 값까지 검색 (파이어스토어 특수 문자를 추가)

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
                        val like = (document["like"] as? Long)?.toInt()
                        val cost = (document["cost"] as? Long)?.toInt()
                        val fishKind = document["fishKind"] as String?
                        val userId = document["userId"] as String?
                        val imageUrl = storeImg ?: "기본 이미지 URL"

                        val marketNameWithHash = "$marketName"
                        val item = Review(content, marketNameWithHash, imageUrl, storeName, rating, region, like, fishKind, cost,userId)
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
    }
    private fun performSearch2(query: String?) {
        if (query.isNullOrBlank()) {
            loadAllReviews()
        } else {
            db.collection("review")
                .orderBy("marketName") // content 필드로 정렬
                .startAt(query) // query로 시작하는 값부터 검색
                .endAt(query + "\uf8ff") // query로 끝나는 값까지 검색 (파이어스토어 특수 문자를 추가)

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
                        val like = (document["like"] as? Long)?.toInt()
                        val cost = (document["cost"] as? Long)?.toInt()
                        val fishKind = document["fishKind"] as String?
                        val userId = document["userId"] as String?
                        val imageUrl = storeImg ?: "기본 이미지 URL"

                        val marketNameWithHash = "$marketName"
                        val item = Review(content, marketNameWithHash, imageUrl, storeName, rating, region, like, fishKind, cost,userId)
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
    }
    private fun performSearch3(query: String?) {
        if (query.isNullOrBlank()) {
            loadAllReviews()
        } else {
            db.collection("review")
                .orderBy("storeName") // content 필드로 정렬
                .startAt(query) // query로 시작하는 값부터 검색
                .endAt(query + "\uf8ff") // query로 끝나는 값까지 검색 (파이어스토어 특수 문자를 추가)

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
                        val like = (document["like"] as? Long)?.toInt()
                        val cost = (document["cost"] as? Long)?.toInt()
                        val fishKind = document["fishKind"] as String?
                        val userId = document["userId"] as String?
                        val imageUrl = storeImg ?: "기본 이미지 URL"

                        val marketNameWithHash = "$marketName"
                        val item = Review(content, marketNameWithHash, imageUrl, storeName, rating, region, like, fishKind, cost,userId)
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
    }


}