package com.example.eatraw.banner

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.eatraw.R

class Banner2Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_banner2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivImage = view.findViewById<ImageView>(R.id.ivImage)


        // Glide를 사용하여 이미지 로드
        Glide.with(this)
            .load(R.drawable.jingjingi) // gif 리소스 ID
            .into(ivImage)

        // 이미지 클릭 이벤트 처리
        ivImage.setOnClickListener {
            // Banner2Activity로 이동
            val intent = Intent(activity, Banner2Activity::class.java)
            startActivity(intent)
        }
    }
}

