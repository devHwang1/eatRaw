package com.example.eatraw

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.eatraw.databinding.ActivityDetailBoxBinding
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


class DetailActivity : AppCompatActivity() {

    //좋아요 관련 변수
    private lateinit var likeBtn : Button
    private lateinit var likeCountText: TextView
    private var liked: Boolean = false
    private lateinit var reviewId: String
    private lateinit var binding : ActivityDetailBoxBinding


    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //좋아요 텍스트뷰 표시 아이디
        likeBtn = findViewById(R.id.btnLike)
        likeCountText = findViewById(R.id.likeInt)


        //좋아요 기본이미지
        likeBtn.setBackgroundResource(R.drawable.thumb)

        val reviewId = "2DhQjBjJgFbNjjafzizz" // 리뷰 ID

        //좋아요 숫자 초기화
        updateLikeCount(reviewId)


        likeBtn.setOnClickListener {
            //좋아요 이미지바꾸기
            if(liked){
                likeBtn.setBackgroundResource(R.drawable.thumb)
                liked = false
            }else{
                likeBtn.setBackgroundResource(R.drawable.thumbfill)
                liked = true
            }

            //Firebase 사용
            val db = FirebaseFirestore.getInstance()
            val ref = db.collection("review").document(reviewId)


            db.runTransaction { transition ->
                val document : DocumentSnapshot
                try {
                    document = transition.get(ref)
                }catch (fetchError: Exception){
                    return@runTransaction null
                }
                val like = document.data?.get("like") as? Long ?: run{
                    //좋아요수를 가져오지 못할때
                    return@runTransaction null
                }

                // 트랜잭션으로 1을 더해줌
                val updatedLike = if (liked) like + 1 else like - 1
                transition.update(ref, "like", updatedLike)
            }.addOnSuccessListener {
                // 성공
                Log.d("DetailActivity", "좋아요를 눌렀다")
                updateLikeCount(reviewId)
            }.addOnFailureListener { error ->
                Log.e("DetailActivity", "Transaction failed: $error")
            }
        }




        val intent = intent
        val reviewContentIntent = intent.getStringExtra("reviewContent")  //댓글내용
        val marketNameIntent = intent.getStringExtra("marketName")        //시장이름
        val storeNameIntent = intent.getStringExtra("storeName")          //가게이름
        val ratingIntent = intent.getDoubleExtra("rating", 0.0)   //별점
        val regionIntent = intent.getStringExtra("region")                  //지역
        val fishKindIntent = intent.getStringExtra("fishKind")               //물고기종류
        val userIdIntent = intent.getStringExtra("userId")     // 회원id
        val imageIntent = intent.getStringExtra("image")        //이미지
        val menuCostIntent = intent.getStringExtra("menuCost")        //메뉴가격


        //유저
        val nicknameInten = intent.getStringExtra("userNickname")
        val UserimageInten = intent.getStringExtra("userImage")


        //파이어베이스사용
        val db = FirebaseFirestore.getInstance()


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



    }


    //좋아요 수 업데이트
    fun updateLikeCount(reviewId: String) {
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("review").document(reviewId)

        ref.get().addOnSuccessListener { document ->
            if(document !=null){
                val likeCount = document.getLong("like") ?: 0
                likeCountText.text = likeCount.toString()
            }else{
                Log.e("DetailActivity", "No such document")
            }
        }.addOnFailureListener { e ->
            Log.e("DetailActivity", "Error updating like count: $e")
        }
    }


}