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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var likeBtn: Button
    private lateinit var likeCountText: TextView
    private var liked: Boolean = false
    private lateinit var reviewId: String
    private lateinit var binding: ActivityDetailBoxBinding

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)
        likeCountText = findViewById(R.id.likeInt)
        likeBtn = findViewById(R.id.btnLike)
        likeCountText = findViewById(R.id.likeInt)
        likeBtn.setBackgroundResource(R.drawable.thumb)

        val bundle = intent.extras
        val reviewId = intent.getStringExtra("reviewId")

        val db = FirebaseFirestore.getInstance()


        likeBtn.setOnClickListener {
            if (liked) {
                likeBtn.setBackgroundResource(R.drawable.thumb)
                liked = false
            } else {
                likeBtn.setBackgroundResource(R.drawable.thumbfill)
                liked = true
            }
            Log.d("Sdfsadfsadf","$reviewId")
            // 리뷰의 좋아요 수 업데이트
            GlobalScope.launch(Dispatchers.Main) {
                if (reviewId != null) {
                    updateLikeCount(reviewId, likeCountText)
                }
            }
        }

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
    suspend fun updateLikeCount(reviewId: String, likeCountText: TextView) {
        val db = FirebaseFirestore.getInstance()

        try {
            withContext(Dispatchers.IO) {
                if(reviewId != null) {
                val querySnapshot = db.collection("reviews")
                    .document(reviewId)
                    .get()
                    .await()

                if (querySnapshot.exists()) {
                    val reviewRef = db.collection("reviews").document()

                    db.runTransaction { transaction ->
                        val currentLikes = transaction.get(reviewRef).getLong("like") ?: 0

                        // 좋아요 수 업데이트
                        transaction.update(reviewRef, "like", FieldValue.increment(1))

                        // "liked" 필드 업데이트 (liked 여부를 나타내는 필드가 있다고 가정)
                        transaction.update(reviewRef, "liked", liked)

                        // 좋아요 수를 반환
                        currentLikes + 1
                    }.addOnSuccessListener { updatedLikes ->
                        // 트랜잭션 성공
                        Log.d("DetailActivity", "좋아요를 눌렀다")

                        // 좋아요 트랜잭션이 성공한 후에 UI 업데이트
                        likeCountText.text = updatedLikes.toString()
                    }.addOnFailureListener { error ->
                        // 트랜잭션 실패
                        Log.e("DetailActivity", "Transaction failed: $error")
                    }
                } else {
                    Log.e("DetailActivity", "No document found with reviewId: $reviewId")
                }
            }}
        } catch (e: Exception) {
            Log.e("DetailActivity", "Error: $e")
        }
    }






    //좋아요 수 업데이트




}
