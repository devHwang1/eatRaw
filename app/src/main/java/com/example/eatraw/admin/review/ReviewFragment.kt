package com.example.eatraw.admin.review

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatraw.adapter.ReviewAdminAdapter
import com.example.eatraw.data.Review
import com.example.eatraw.databinding.FragmentReviewBinding
import com.google.firebase.firestore.FirebaseFirestore

class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReviewAdminAdapter
    private val reviewList = mutableListOf<Review>()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val reviewViewModel =
            ViewModelProvider(this).get(ReviewViewModel::class.java)

        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // 리사이클러뷰 초기화 및 어댑터 설정
        recyclerView = binding.recyclerViewReviewList
        adapter = ReviewAdminAdapter(requireContext(), reviewList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // Firestore에서 데이터 가져오고 정렬하기
        firestore.collection("review")
            .get()
            .addOnSuccessListener { result ->
                val newItems = mutableListOf<Review>()
                for (document in result) {
                    val content = document["content"] as String
                    val marketName = document["marketName"] as String
                    val storeName = document["storeName"] as String
                    val rating =
                        document["rating"]?.toString()?.toDoubleOrNull() // rating을 Float로 가져옴
                    val storeImg = document["storeImg"] as String?
                    val region = document["region"] as String?
                    val like = (document["like"] as? Long)?.toInt() // "like" 필드를 Int로 가져오기
                    val cost = (document["cost"] as? Long)?.toInt()
                    val fishKind = document["fishKind"] as String?
                    val userId = document["userId"] as String?

                    // 이미지 URL이 없으면 기본 이미지 URL로 대체
                    val imageUrl = storeImg ?: "기본 이미지 URL" // 여기에 기본 이미지 URL을 넣으세요

                    val marketNameWithHash = "#$marketName"
                    val item = Review(
                        content,
                        marketNameWithHash,
                        imageUrl,
                        storeName,
                        rating,
                        region,
                        like,
                        fishKind,
                        cost,
                        userId
                    )
                    newItems.add(item)
                }

                // 주석 처리된 부분은 삭제
                adapter.setData(newItems)
            }
            .addOnFailureListener { exception ->
                Log.w("ReviewActivity", "Error: $exception")
            }



        // 검색창 초기화
        val reviewSearchbar = binding.reviewSearchbar
        reviewSearchbar.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색어를 기반으로 데이터 가져오기
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 텍스트가 변경될 때마다 호출
                // 여기서도 검색어를 기반으로 데이터 가져오기를 수행할 수 있어.
                performSearch(newText)
                return true
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun performSearch(query: String?) {
        if (query.isNullOrBlank()) {
            // 검색어가 비어있거나 공백인 경우 전체 목록을 보여줍니다.
            showAllReviews()
        } else {
            val uppercaseQuery = query.toUpperCase() // 대소문자 구분 없이 검색하기 위해 대문자로 변환

            // Firestore에서 검색 쿼리 수행
            firestore.collection("review")
                .orderBy("storeName") // storeName 필드로 정렬
                .startAt(query) // query로 시작하는 값부터 검색
                .endAt(query + "\uf8ff") // query로 끝나는 값까지 검색 (파이어스토어 특수 문자를 추가)
                .get()
                .addOnSuccessListener { result ->
                    val searchResult = mutableListOf<Review>()

                    for (document in result) {
                        val content = document["content"] as String
                        val marketName = document["marketName"] as String
                        val storeName = document["storeName"] as String
                        val rating =
                            document["rating"]?.toString()?.toDoubleOrNull() // rating을 Float로 가져옴
                        val storeImg = document["storeImg"] as String?
                        val region = document["region"] as String?
                        val like = (document["like"] as? Long)?.toInt() // "like" 필드를 Int로 가져오기
                        val cost = (document["cost"] as? Long)?.toInt()
                        val fishKind = document["fishKind"] as String?
                        val userId = document["userId"] as String?

                        // 이미지 URL이 없으면 기본 이미지 URL로 대체
                        val imageUrl = storeImg ?: "기본 이미지 URL" // 여기에 기본 이미지 URL을 넣으세요

                        val marketNameWithHash = "#$marketName"
                        val item = Review(
                            content,
                            marketNameWithHash,
                            imageUrl,
                            storeName,
                            rating,
                            region,
                            like,
                            fishKind,
                            cost,
                            userId
                        )
                        searchResult.add(item)
                    }

                    // 어댑터에 검색 결과를 설정하고 리사이클러뷰를 업데이트
                    adapter.setData(searchResult)
                }
                .addOnFailureListener { exception ->
                    // 데이터 가져오기 실패 시 처리
                    Log.e("FirestoreError", "Error getting documents: ", exception)
                }
        }
    }

    private fun showAllReviews() {
        // 전체 리뷰 목록을 보여주는 로직을 추가해도 좋아.
        // 예를 들면, 원래의 데이터를 다시 로드하거나 기존 목록을 보여주는 방식으로 구현 가능해.
        firestore.collection("review")
            .get()
            .addOnSuccessListener { result ->
                val allReviews = mutableListOf<Review>()

                for (document in result) {
                    val content = document["content"] as String
                    val marketName = document["marketName"] as String
                    val storeName = document["storeName"] as String
                    val rating =
                        document["rating"]?.toString()?.toDoubleOrNull() // rating을 Float로 가져옴
                    val storeImg = document["storeImg"] as String?
                    val region = document["region"] as String?
                    val like = (document["like"] as? Long)?.toInt() // "like" 필드를 Int로 가져오기
                    val cost = (document["cost"] as? Long)?.toInt()
                    val fishKind = document["fishKind"] as String?
                    val userId = document["userId"] as String?

                    // 이미지 URL이 없으면 기본 이미지 URL로 대체
                    val imageUrl = storeImg ?: "기본 이미지 URL" // 여기에 기본 이미지 URL을 넣으세요

                    val marketNameWithHash = "#$marketName"
                    val item = Review(
                        content,
                        marketNameWithHash,
                        imageUrl,
                        storeName,
                        rating,
                        region,
                        like,
                        fishKind,
                        cost,
                        userId
                    )
                    allReviews.add(item)
                }

                // 어댑터에 전체 리뷰 목록을 설정하고 리사이클러뷰를 업데이트
                adapter.setData(allReviews)
            }
            .addOnFailureListener { exception ->
                // 데이터 가져오기 실패 시 처리
                Log.e("FirestoreError", "Error getting documents: ", exception)
            }
    }

}