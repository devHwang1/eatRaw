package com.example.eatraw.admin.users

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
import com.example.eatraw.adapter.UsersAdapter
import com.example.eatraw.data.Users
import com.example.eatraw.databinding.FragmentUsersBinding
import com.google.firebase.firestore.FirebaseFirestore

class UsersFragment : Fragment() {

    private var _binding: FragmentUsersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UsersAdapter
    private val userList = mutableListOf<Users>()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val usersViewModel =
            ViewModelProvider(this).get(UsersViewModel::class.java)

        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 리사이클러뷰 초기화 및 어댑터 설정
        recyclerView = binding.recyclerViewUsersList
        adapter = UsersAdapter(requireContext(), userList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // Firestore에서 Users 데이터 가져오기
        firestore.collection("users")
            .orderBy("email")
            .get()
            .addOnSuccessListener { documents ->
                val usersList = mutableListOf<Users>()

                for (document in documents) {
                    val email = document.getString("email")
                    val nickname = document.getString("nickname")
                    val aouthLogin = document.getBoolean("aouthLogin") ?: false
                    val admin = document.getBoolean("admin") ?: false
                    val imageUrl = document.getString("imageUrl")
                    val userId = document.getString("userId")
                    val likeMarket =
                        document.getString("likeMarket") // 이 부분이 "선호 시장" 정보를 가져오는 부분입니다.

                    if (email != null && nickname != null) {
                        val user = Users(
                            email, nickname, aouthLogin, admin,
                            imageUrl ?: "", userId = "", likeMarket = likeMarket
                        )
                        usersList.add(user)
                    }
                }

                // RecyclerView 어댑터에 데이터 설정
                adapter.setData(usersList)
            }
            .addOnFailureListener { exception ->
                // 데이터 가져오기 실패 시 처리
                Log.e("FirestoreError", "Error getting documents: ", exception)
            }


        // 검색창 초기화
        val searchView = binding.userSearchbar
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
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
            showAllUsers()
        } else {
            val lowercaseQuery = query.lowercase() // 검색어를 소문자로 변환
            val uppercaseQuery = query.uppercase() // 검색어를 대문자로 변환

            // Firestore에서 소문자로 검색 쿼리 수행
            firestore.collection("users")
                .orderBy("email")
                .startAt(lowercaseQuery)
                .endAt(lowercaseQuery + "\uf8ff")
                .get()
                .addOnSuccessListener { documents ->
                    val searchResult = mutableListOf<Users>()

                    for (document in documents) {
                        val email = document.getString("email")
                        val nickname = document.getString("nickname")
                        val aouthLogin = document.getBoolean("aouthLogin") ?: false
                        val admin = document.getBoolean("admin") ?: false
                        val imageUrl = document.getString("imageUrl")
                        val userId = document.getString("userId")
                        val likeMarket = document.getString("likeMarket")
                        if (email != null && nickname != null) {
                            val user = Users(
                                email,
                                nickname,
                                aouthLogin,
                                admin,
                                imageUrl ?: "",
                                userId = "",
                                likeMarket = likeMarket
                            )
                            searchResult.add(user)
                        }
                    }

                    // Firestore에서 대문자로 검색 쿼리 수행
                    firestore.collection("users")
                        .orderBy("email")
                        .startAt(uppercaseQuery)
                        .endAt(uppercaseQuery + "\uf8ff")
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                val email = document.getString("email")
                                val nickname = document.getString("nickname")
                                val aouthLogin = document.getBoolean("aouthLogin") ?: false
                                val admin = document.getBoolean("admin") ?: false
                                val imageUrl = document.getString("imageUrl")
                                val userId = document.getString("userId")
                                val likeMarket = document.getString("likeMarket")
                                if (email != null && nickname != null) {
                                    val user = Users(
                                        email,
                                        nickname,
                                        aouthLogin,
                                        admin,
                                        imageUrl ?: "",
                                        userId = "",
                                        likeMarket = likeMarket
                                    )
                                    searchResult.add(user)
                                }
                            }

                            // 어댑터에 검색 결과를 설정하고 리사이클러뷰를 업데이트
                            adapter.setData(searchResult)
                            adapter.notifyDataSetChanged()
                        }
                        .addOnFailureListener { exception ->
                            // 데이터 가져오기 실패 시 처리
                            Log.e("FirestoreError", "Error getting documents: ", exception)
                        }
                }
                .addOnFailureListener { exception ->
                    // 데이터 가져오기 실패 시 처리
                    Log.e("FirestoreError", "Error getting documents: ", exception)
                }
        }
    }


    private fun showAllUsers() {
        firestore.collection("users")
            .orderBy("email")
            .get()
            .addOnSuccessListener { documents ->
                val usersList = mutableListOf<Users>()

                for (document in documents) {
                    val email = document.getString("email")
                    val nickname = document.getString("nickname")
                    val aouthLogin = document.getBoolean("aouthLogin") ?: false
                    val admin = document.getBoolean("admin") ?: false
                    val imageUrl = document.getString("imageUrl")
                    val userId = document.getString("userId")
                    val likeMarket =
                        document.getString("likeMarket") // 이 부분이 "선호 시장" 정보를 가져오는 부분입니다.

                    if (email != null && nickname != null) {
                        val user = Users(
                            email, nickname, aouthLogin, admin,
                            imageUrl ?: "", userId = "", likeMarket = likeMarket
                        )
                        usersList.add(user)
                    }
                }

                // RecyclerView 어댑터에 데이터 설정
                adapter.setData(usersList)

                // 데이터가 갱신되었음을 어댑터에 알림
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // 데이터 가져오기 실패 시 처리
                Log.e("FirestoreError", "Error getting documents: ", exception)
            }
    }
}