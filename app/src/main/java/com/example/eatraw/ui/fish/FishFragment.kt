package com.example.eatraw.ui.fish

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatraw.AddFishActivity
import com.example.eatraw.ComparingPriceListActivity
import com.example.eatraw.R
import com.example.eatraw.adapter.FishAdapter
import com.example.eatraw.data.ComparingPriceItem
import com.example.eatraw.databinding.FragmentFishBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

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
            .get()
            .addOnSuccessListener { documents ->
                val fishData  = mutableListOf<ComparingPriceItem>()

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
                            fishList.add(comparingPriceItem)

                            // 데이터가 변경되었음을 어댑터에 알림
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                // 데이터 가져오기 실패 시 처리
                Log.e("FirestoreError", "Error getting documents: ", exception)
            }

        val btnAddFish = binding.btnAddFish
        btnAddFish.setOnClickListener {
            val intent = Intent(requireContext(), AddFishActivity::class.java)
            startActivity(intent)
        }



        val textView: TextView = binding.textFish
        fishViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
