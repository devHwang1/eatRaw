package com.example.eatraw.ui.users

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
import com.example.eatraw.R
import com.example.eatraw.adapter.FishAdapter
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
            .orderBy("admin")
            .get()
            .addOnSuccessListener { documents ->
                val usersList = mutableListOf<Users>()

                for (document in documents) {
                    val email = document.getString("email")
                    val nickname = document.getString("nickname")
                    val aouthLogin = document.getBoolean("aouthLogin") ?: false
                    val admin = document.getBoolean("admin") ?: false
                    val imageUrl = document.getString("imageUrl")

                    if (email != null && nickname != null) {
                        val user = Users(email, nickname, aouthLogin, admin, imageUrl ?: "")
                        usersList.add(user)
                    }
                }

                // RecyclerView 어댑터에 데이터 설정
                val adapter = UsersAdapter(requireContext(), usersList)
                recyclerView.adapter = adapter

                // 데이터가 갱신되었음을 어댑터에 알림
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // 데이터 가져오기 실패 시 처리
                Log.e("FirestoreError", "Error getting documents: ", exception)
            }




        val textView: TextView = binding.textUsers
        usersViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}