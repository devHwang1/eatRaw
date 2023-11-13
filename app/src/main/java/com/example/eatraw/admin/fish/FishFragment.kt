package com.example.eatraw.admin.fish

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatraw.AddFishActivity
import com.example.eatraw.adapter.FishAdapter
import com.example.eatraw.data.ComparingPriceItem
import com.example.eatraw.databinding.FragmentFishBinding
import com.google.firebase.firestore.FirebaseFirestore

class FishFragment : Fragment() {

    private var _binding: FragmentFishBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FishAdapter
    private val fishList = mutableListOf<ComparingPriceItem>()
    private val firestore = FirebaseFirestore.getInstance()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fishViewModel = ViewModelProvider(this).get(FishViewModel::class.java)

        _binding = FragmentFishBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 리사이클러뷰 초기화 및 어댑터 설정
        recyclerView = binding.recyclerViewFishList
        adapter = FishAdapter(requireContext(), fishList) // FishAdapter를 사용
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // Firestore에서 데이터 가져오고 정렬하기
        firestore.collection("fish")
            .orderBy("f_season")
            .get()
            .addOnSuccessListener { documents ->
                val fishData = mutableListOf<ComparingPriceItem>()

                for (document in documents) {
                    val fishName = document.getString("f_name")
                    val count = (document["f_count"] as? Long)?.toInt() ?: 0
                    val minCost = (document["f_min"] as? Long)?.toInt()
                    val avgCost = (document["f_avg"] as? Long)?.toInt()
                    val maxCost = (document["f_max"] as? Long)?.toInt()
                    val fishImg = document.getString("f_img")
                    val season = document.getString("f_season")

//                    Log.e("피쉬이미지>>", "${fishImg}")

                    if (fishName != null && minCost != null && avgCost != null && maxCost != null && fishImg != null) {
                        // 이미지의 다운로드 URL 생성
//                        val imageUrl = "https://firebasestorage.googleapis.com/v0/b/androidtest-e6bbe.appspot.com/o/FishImg%2F$fishImg?alt=media"
//                        Log.e(">>", "${imageUrl}")
                        val comparingPriceItem = ComparingPriceItem(
                            fishName,
                            count,
                            minCost.toLong(),
                            avgCost.toLong(),
                            maxCost.toLong(),
                            fishImg, // 위에서 생성한 이미지 URL 사용
                            season
                        )
                        fishList.add(comparingPriceItem)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // 데이터 가져오기 실패 시 처리
                Log.e("FirestoreError", "Error getting documents: ", exception)
            }

        val searchView = binding.fishSearchbar

        // SearchView에 텍스트가 변경될 때마다 호출되는 리스너
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 사용자가 엔터 키를 눌렀을 때 호출됨
                // 여기에서 Firestore에서 검색을 수행하고 결과를 리사이클러뷰에 업데이트하는 로직을 추가할 수 있습니다.
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // SearchView에 입력이 있을 때마다 호출됨
                // 여기에서 Firestore에서 검색을 수행하고 결과를 리사이클러뷰에 업데이트하는 로직을 추가할 수 있습니다.
                performSearch(newText)
                return true
            }
        })

        val btnAddFish = binding.btnAddFish
        btnAddFish.setOnClickListener {
            val intent = Intent(requireContext(), AddFishActivity::class.java)
            startActivity(intent)
        }



//        val textView: TextView = binding.textFish
//        fishViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun performSearch(query: String?) {
        if (query.isNullOrBlank()) {
            // 검색어가 비어있거나 공백인 경우 전체 목록을 보여줍니다.
            showAllFish()
        } else {
            // Firestore에서 검색 쿼리 수행
            val uppercaseQuery = query.toUpperCase() // 대소문자 구분 없이 검색하기 위해 대문자로 변환

            firestore.collection("fish")
                .orderBy("f_name")
                .startAt(uppercaseQuery)
                .endAt(uppercaseQuery + "\uf8ff")
                .get()
                .addOnSuccessListener { documents ->
                    val searchResult = mutableListOf<ComparingPriceItem>()

                    val fishData = mutableListOf<ComparingPriceItem>()

                    for (document in documents) {
                        val fishName = document.getString("f_name")
                        val count = (document["f_count"] as? Long)?.toInt() ?: 0
                        val minCost = (document["f_min"] as? Long)?.toInt()
                        val avgCost = (document["f_avg"] as? Long)?.toInt()
                        val maxCost = (document["f_max"] as? Long)?.toInt()
                        val fishImg = document.getString("f_img")
                        val season = document.getString("f_season")

                        Log.e("피쉬이미지>>", "${fishImg}")

                        if (fishName != null && minCost != null && avgCost != null && maxCost != null && fishImg != null) {
                            // 이미지의 다운로드 URL 생성
//                        val imageUrl = "https://firebasestorage.googleapis.com/v0/b/androidtest-e6bbe.appspot.com/o/FishImg%2F$fishImg?alt=media"
//                        Log.e(">>", "${imageUrl}")
                            val comparingPriceItem = ComparingPriceItem(
                                fishName,
                                count,
                                minCost.toLong(),
                                avgCost.toLong(),
                                maxCost.toLong(),
                                fishImg, // 위에서 생성한 이미지 URL 사용
                                season
                            )
                            searchResult.add(comparingPriceItem)
                        }
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


    private fun showAllFish() {
        // 전체 목록을 보여주기 위해 Firestore에서 전체 데이터를 가져와 fishList에 설정
        firestore.collection("fish")
            .orderBy("f_season")
            .get()
            .addOnSuccessListener { documents ->
                val allFish = mutableListOf<ComparingPriceItem>()

                val fishData = mutableListOf<ComparingPriceItem>()

                for (document in documents) {
                    val fishName = document.getString("f_name")
                    val count = (document["f_count"] as? Long)?.toInt() ?: 0
                    val minCost = (document["f_min"] as? Long)?.toInt()
                    val avgCost = (document["f_avg"] as? Long)?.toInt()
                    val maxCost = (document["f_max"] as? Long)?.toInt()
                    val fishImg = document.getString("f_img")
                    val season = document.getString("f_season")

                    Log.e("피쉬이미지>>", "${fishImg}")

                    if (fishName != null && minCost != null && avgCost != null && maxCost != null && fishImg != null) {
                        // 이미지의 다운로드 URL 생성
//                        val imageUrl = "https://firebasestorage.googleapis.com/v0/b/androidtest-e6bbe.appspot.com/o/FishImg%2F$fishImg?alt=media"
//                        Log.e(">>", "${imageUrl}")
                        val comparingPriceItem = ComparingPriceItem(
                            fishName,
                            count,
                            minCost.toLong(),
                            avgCost.toLong(),
                            maxCost.toLong(),
                            fishImg, // 위에서 생성한 이미지 URL 사용
                            season
                        )
                        allFish.add(comparingPriceItem)
                    }
                }

                // 어댑터에 전체 목록을 설정하고 리사이클러뷰를 업데이트
                adapter.setData(allFish)
            }
            .addOnFailureListener { exception ->
                // 데이터 가져오기 실패 시 처리
                Log.e("FirestoreError", "Error getting documents: ", exception)
            }
    }

}
