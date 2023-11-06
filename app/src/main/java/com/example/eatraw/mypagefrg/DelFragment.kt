package com.example.eatraw.mypagefrg

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eatraw.LoginActivity
import com.example.eatraw.databinding.FragmentDelBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DelFragment : Fragment() {

    private lateinit var binding: FragmentDelBinding
    private lateinit var password: TextInputEditText
    private lateinit var mAuth: FirebaseAuth
    private lateinit var button: Button
    private lateinit var btnBack:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDelBinding.inflate(inflater, container, false)
        password = binding.password
        button = binding.btnRegister
        btnBack = binding.btnBack

        btnBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        button.setOnClickListener {
            val passwordCheck = password.text.toString()
            val currentUser = mAuth.currentUser

            if (passwordCheck.isEmpty()) {
                Toast.makeText(context, "패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (currentUser != null) {
                mAuth.signInWithEmailAndPassword(currentUser.email!!, passwordCheck)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // 로그인이 성공한 경우
                            val userId = currentUser.uid
                            val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)

                            userRef.delete()
                                .addOnSuccessListener {
                                    // Firestore에서 사용자 정보 삭제 성공
                                    currentUser.delete()
                                        .addOnCompleteListener { deleteTask ->
                                            if (deleteTask.isSuccessful) {
                                                // 계정 삭제 완료
                                                Toast.makeText(
                                                    context,
                                                    "계정이 삭제되었습니다.",
                                                    Toast.LENGTH_SHORT

                                                ).show()
                                                val intent = Intent(context, LoginActivity::class.java)
                                                startActivity(intent)
                                                activity?.finish()
                                            } else {
                                                // 계정 삭제 실패
                                                Toast.makeText(
                                                    context,
                                                    "계정 삭제에 실패했습니다.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                }
                                .addOnFailureListener { exception ->
                                    // Firestore에서 사용자 정보 삭제 실패
                                    Toast.makeText(
                                        context,
                                        "Firestore에서 사용자 정보 삭제에 실패했습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else {
                            // 로그인이 실패한 경우
                            Toast.makeText(context, "패스워드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        return binding.root
    }
}