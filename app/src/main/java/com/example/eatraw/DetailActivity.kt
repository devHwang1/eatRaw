package com.example.eatraw

import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.eatraw.databinding.ActivityDetailBoxBinding


class DetailActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val reviewContentIntent = intent.getStringExtra("reviewContent")  //댓글내용
        val marketNameIntent = intent.getStringExtra("marketName")        //시장이름
        val storeNameIntent = intent.getStringExtra("storeName")          //가게이름
        val ratingIntent = intent.getDoubleExtra("rating", 0.0)   //별점
        val regionIntent = intent.getStringExtra("region")        //지역
        val fishKindIntent = intent.getStringExtra("fishKind")        //가게가격
        val costIntent = intent.getStringExtra("cost")        //물고기종류
        val userIdIntent = intent.getStringExtra("userId")     // 회원id
        val imageIntent = intent.getStringExtra("image")    //이미지

        //유저
        val nicknameInten = intent.getStringExtra("userNickname")
        val UserimageInten = intent.getStringExtra("userImage")

        //물고기
        val fishMinIntent = intent.getStringExtra("fishMin")
        val fishAvgIntent = intent.getStringExtra("fishAvg")
        val fishMaxIntent = intent.getStringExtra("fishMax")




        // 데이터를 TextView에 설정
        val reviewContent = findViewById<TextView>(R.id.contentView)
//        val storeName = findViewById<TextView>(R.id.storeNameBar)
        val rating = findViewById<TextView>(R.id.mStarsocore)
        val fishKind = findViewById<TextView>(R.id.StorePriceInt)
        val cost = findViewById<TextView>(R.id.MenuFishName)
        val userNicName = findViewById<TextView>(R.id.mName)
        val Image = findViewById<ImageView>(R.id.Reviewimg)
        val userimg = findViewById<ImageView>(R.id.mImg)

        //물고기 TextView에 설정
        val fishMinText = findViewById<TextView>(R.id.IntMin)
        val fishAvgText = findViewById<TextView>(R.id.IntAvg)
        val fishMaxText = findViewById<TextView>(R.id.IntMax)

        //별모양
        val DratingBar = findViewById<RatingBar>(R.id.DratingBar)
        DratingBar.rating = ratingIntent.toFloat()


        //리뷰
        reviewContent.text ="$reviewContentIntent"
//        storeName.text = "$storeNameIntent"
        rating.text = "$ratingIntent"
        fishKind.text = "$fishKindIntent"
        cost.text = "$costIntent"
        userNicName.text = "$nicknameInten"

        //몰고기
        fishMinText.text = "$fishMinIntent"
        fishAvgText.text = "$fishAvgIntent"
        fishMaxText.text= "$fishMaxIntent"


        //이미지 설정
        Glide.with(this)
            .load(imageIntent)
            .into(Image)

        //이미지 설정(유저)
        Glide.with(this)
            .load(UserimageInten)
            .into(userimg)


    }


}