package com.example.eatraw

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.eatraw.databinding.ActivityMypageBinding

class MypageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMypageBinding
    private lateinit var nickhello: String
    private lateinit var email: String
    private lateinit var thumbnail: ImageView






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val transaction = supportFragmentManager.beginTransaction()





    }
}