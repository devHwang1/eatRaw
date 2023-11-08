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
import com.example.eatraw.databinding.FragmentGoogleDelBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GoogleDelFragment : Fragment() {

    private lateinit var binding: FragmentGoogleDelBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var btnRegister: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoogleDelBinding.inflate(inflater, container, false)
        btnRegister = binding.btnRegister
        btnBack = binding.btnBack

        btnBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }


        btnRegister.setOnClickListener {

            // 구글 로그인 데이터 삭제
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
            val currentUser = mAuth.currentUser

            val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
            googleSignInClient.revokeAccess()
                .addOnCompleteListener {
                    // 구글 로그인 데이터 삭제 완료
                    Toast.makeText(context, "구글 로그인 데이터 삭제 완료", Toast.LENGTH_SHORT).show()
                }

            val userId = currentUser?.uid
            val userRef = FirebaseFirestore.getInstance().collection("users").document(userId!!)

            userRef.delete()
                .addOnSuccessListener {
                    // Firestore에서 사용자 정보 삭제 성공
                    currentUser?.delete()?.addOnCompleteListener { deleteTask ->
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
        }

        return binding.root
    }
}