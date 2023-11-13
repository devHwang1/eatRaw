package com.example.eatraw

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.eatraw.databinding.ActivityDetailBoxBinding
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    //좋아요 관련 변수
//    private lateinit var likeBtn: Button
//    private lateinit var likeCountText: TextView
//    private var liked: Boolean = false
    private lateinit var reviewId: String
    private lateinit var binding: ActivityDetailBoxBinding


    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle = intent.extras
        val reviewId = intent.getStringExtra("reviewId")
        val db = FirebaseFirestore.getInstance()


        val intent = intent
        val reviewContentIntent = intent.getStringExtra("reviewContent")  //댓글내용
        val marketNameIntent = intent.getStringExtra("marketName")        //시장이름
        val storeNameIntent = intent.getStringExtra("storeName")          //가게이름
        val ratingIntent = intent.getDoubleExtra("rating", 0.0)   //별점
        val regionIntent = intent.getStringExtra("region")                  //지역
        val fishKindIntent = intent.getStringExtra("fishKind")               //물고기종류
        val userIdIntent = intent.getStringExtra("userId")     // 회원id
        val imageIntent = intent.getStringExtra("image")        //이미지
        val menuCostIntent = intent.getIntExtra("cost", 0)        //메뉴가격


        //유저
        val nicknameInten = intent.getStringExtra("userNickname")
        val UserimageInten = intent.getStringExtra("userImage")


        //파이어베이스사용


        //물고기종류에 따른 가격가져오기
        val fishkindCostIntent = intent.getStringExtra("fishKind")
        db.collection("fish")
            .whereEqualTo("f_name", fishkindCostIntent)
            .get()
            .addOnSuccessListener { fishDocuments ->
                if (!fishDocuments.isEmpty) {
                    val fishDocument = fishDocuments.documents[0]
                    val fishMin = fishDocument.getLong("f_min")
                    val fishAvg = fishDocument.getLong("f_avg")
                    val fishMax = fishDocument.getLong("f_max")

                    val fishMinText = findViewById<TextView>(R.id.IntMin)
                    val fishAvgText = findViewById<TextView>(R.id.IntAvg)
                    val fishMaxText = findViewById<TextView>(R.id.IntMax)

                    fishMinText.text = fishMin.toString()
                    fishAvgText.text = fishAvg.toString()
                    fishMaxText.text = fishMax.toString()

                    // menuCostIntent 값과 fishAvg 값을 비교하여 문구 업데이트
                    val menuCostIntent = intent.getIntExtra("cost", 0)
                    val menuCostTextView = findViewById<TextView>(R.id.Textcomparison)

                    if (fishAvg != null) {
                        if (menuCostIntent < fishAvg.toInt()) {
                            menuCostTextView.text = "가격이 평균보다 낮습니다."
                        } else if (menuCostIntent > fishAvg.toInt()) {
                            menuCostTextView.text = "가격이 평균보다 높습니다."
                        } else {
                            menuCostTextView.text = "가격이 평균과 같습니다."
                        }
                    }
                } else {
                    Log.e("FirestoreError", "Error getting fish document: ")
                }


            }


        // 데이터를 TextView에 설정
        val reviewContent = findViewById<TextView>(R.id.contentView)
//        val storeName = findViewById<TextView>(R.id.storeNameBar)
        val rating = findViewById<TextView>(R.id.mStarsocore)
        val userNicName = findViewById<TextView>(R.id.mName)
        val Image = findViewById<ImageView>(R.id.Reviewimg)
        val userimg = findViewById<ImageView>(R.id.mImg)
        val menuCost = findViewById<TextView>(R.id.StorePriceInt)


        //물고기 TextView에 설정
        val fishKinName = findViewById<TextView>(R.id.MenuFishName)


        //별모양
        val DratingBar = findViewById<RatingBar>(R.id.DratingBar)
        DratingBar.rating = ratingIntent.toFloat()


        //리뷰
        reviewContent.text = "$reviewContentIntent"
//        storeName.text = "$storeNameIntent"
        rating.text = "$ratingIntent"
        userNicName.text = "$nicknameInten"
        menuCost.text = "$menuCostIntent"

        //몰고기 이름
        fishKinName.text = "$fishKindIntent"


        //이미지 설정
        Glide.with(this)
            .load(imageIntent)
            .into(Image)

        //이미지 설정(유저)
        Glide.with(this)
            .load(UserimageInten)
            .into(userimg)

        var bnv_main = findViewById(R.id.bnv_main) as BottomNavigationView

        // OnNavigationItemSelectedListener를 통해 탭 아이템 선택 시 이벤트를 처리
        // navi_menu.xml 에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.
        bnv_main.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.first -> {
                        // 다른 액티비티로 이동
                        val intent = Intent(this@DetailActivity, MainActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.second -> {
                        // 다른 액티비티로 이동
                        val intent = Intent(this@DetailActivity, ReviewActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.third -> {
                        // 다른 액티비티로 이동
                        val intent =
                            Intent(this@DetailActivity, ComparingPriceListActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.four -> {
                        // 다른 액티비티로 이동
                        val intent = Intent(this@DetailActivity, MypageActivity::class.java)
                        startActivity(intent)
                    }
                }
                true
            }
        }

        //몰고기 이름
        fishKinName.text = "$fishKindIntent"

        //이미지 설정
        Glide.with(this)
            .load(imageIntent)
            .into(Image)

        //이미지 설정(유저)
        Glide.with(this)
            .load(UserimageInten)
            .into(userimg)

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

