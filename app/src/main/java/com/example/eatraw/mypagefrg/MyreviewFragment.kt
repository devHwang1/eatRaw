package com.example.eatraw.mypagefrg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.detailRecylerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val noReviewsTextView = binding.noReviewsTextView

        // 데이터를 불러오고 어댑터를 설정하는 로직을 onViewCreated에서 수행
        lifecycleScope.launch {
            reviews = getAllReviewsFromDb()
            users = getAllUsersFromDb()

            // 현재 로그인한 사용자의 ID를 가져오기
            val currentUserId = mAuth.currentUser!!.uid
            val userReviews = reviews.filter { it.userId == currentUserId }

            if (userReviews.isEmpty()) {
                noReviewsTextView.visibility = View.VISIBLE
            } else {
                withContext(Dispatchers.Main) {
                    val adapter = MyReviewAdapter(userReviews, users)
                    recyclerView.adapter = adapter
                }
            }
        }
    }

    private suspend fun getAllReviewsFromDb(): List<Review> {
        return withContext(Dispatchers.IO) {
            val reviews = mutableListOf<Review>()
            val docRef = db.collection("review")

            try {
                val documents = docRef.get().await()
                for (document in documents) {
                    val content = document.getString("content")
                    val marketName = document.getString("marketName")
                    val storeImg = document.getString("storeImg")
                    val storeName = document.getString("storeName")
                    val rating = document.getDouble("rating")
                    val region = document.getString("region")
                    val like = document.getLong("like")
                    val fishKind = document.getString("fishKind")
                    val cost = document.getLong("cost")
                    val userId = document.getString("userId")


                    if (content != null && rating != null && marketName != null && userId != null && storeImg != null
                        && storeName != null && storeName != null && fishKind != null && cost != null
                    ) {
                        val review = Review(content,marketName,storeImg,storeName,rating.toDouble(), region,
                            like?.toInt(),fishKind,cost.toInt(),userId)
                        reviews.add(review)
                    }
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
                    val email = document.getString("email")
                    val nickname = document.getString("nickname")
                    val aouthLogin = document.getBoolean("aouthLogin")
                    val admin = document.getBoolean("admin")
                    val imageUrl = document.getString("imageUrl")
                    val userId = document.getString("userId")

                    if (email != null && nickname != null && aouthLogin != null && admin != null&&  admin != null && userId != null) {
                        val user = Users(email, nickname, aouthLogin, admin, imageUrl, userId)
                        users.add(user)
                    }
                }

                // 어댑터 초기화 및 설정
                println("Loaded ${users.size} users")
            } catch (exception: Exception) {
                println("Error getting documents: $exception")
            }
            return@withContext users
        }

    }

}


