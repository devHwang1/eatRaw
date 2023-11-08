package com.example.eatraw.mypagefrg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eatraw.R
import com.example.eatraw.databinding.FragmentNickBinding
import com.example.eatraw.databinding.FragmentPasswordBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class PasswordFragment : Fragment() {

    private lateinit var binding: FragmentPasswordBinding
    private lateinit var password: TextInputEditText
    private lateinit var button: Button
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPasswordBinding.inflate(inflater, container, false)
        password = binding.password
        button = binding.btnRegister



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
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, ModifyFragment()).addToBackStack(null).commit()
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