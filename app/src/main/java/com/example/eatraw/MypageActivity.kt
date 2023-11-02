package com.example.eatraw

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.eatraw.data.BannerItem
import com.example.eatraw.databinding.ActivityMypageBinding

class MypageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMypageBinding
    private lateinit var nickhello: String
    private lateinit var email: String
    private lateinit var thumbnail: ImageView
    private lateinit var recyclerViewMypage: RecyclerView

    private val MypageData: List<BannerItem> = listOf(
        BannerItem(R.drawable.banner1),
        BannerItem(R.drawable.banner2),
        BannerItem(R.drawable.banner1),
        BannerItem(R.drawable.banner2),
        BannerItem(R.drawable.banner1),
        BannerItem(R.drawable.banner2)
        // 추가적인 BannerItem 인스턴스와 설명, 타이틀을 추가하세요.
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewMypage



    }
}