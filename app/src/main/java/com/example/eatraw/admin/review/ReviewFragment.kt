package com.example.eatraw.admin.review

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    // This property is only valid between onCreateView and
    // onDestroyView.
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
                        cost,
                        fishKind,
                        userId
                    )
                    newItems.add(item)
                }

                reviewList.clear()
                reviewList.addAll(newItems)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("ReviewActivity", "Error: $exception")
            }


//        val textView: TextView = binding.textReview
//        reviewViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}