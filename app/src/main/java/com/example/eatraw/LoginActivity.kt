package com.example.eatraw

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonLogin: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView

    // 작업을 초기화할 때 사용자가 현재 로그인되어 있는지 확인합니다.
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        buttonLogin = findViewById(R.id.btn_login)
        progressBar = findViewById(R.id.progressBar)
        textView = findViewById(R.id.registerNow)

        textView.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }



        buttonLogin.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()



            if (email.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 사용자가 앱에 로그인하면 다음과 같이 사용자의 이메일 주소와 비밀번호를 signInWithEmailAndPassword에 전달합니다.
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext,
                            "Login Successful.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {

                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }
        }
    }
}