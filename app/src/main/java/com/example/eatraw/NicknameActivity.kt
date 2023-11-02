package com.example.eatraw

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.eatraw.databinding.ActivityNicknameBinding

class NicknameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNicknameBinding
    private lateinit var Nickname: String
    private lateinit var thumNail: String
    private lateinit var btn_register: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNicknameBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        btn_register = findViewById(R.id.btn_register)

        btn_register.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}