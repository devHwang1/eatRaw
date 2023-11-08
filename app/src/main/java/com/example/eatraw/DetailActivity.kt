package com.example.eatraw

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.eatraw.databinding.ActivityDetailBoxBinding
import com.google.firebase.firestore.FirebaseFirestore


class DetailActivity : AppCompatActivity() {

<<<<<<< HEAD
=======

>>>>>>> e6343df945f4d1bf2f683aaffc2611f55bbb3128
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val reviewContentIntent = intent.getStringExtra("reviewContent")  //댓글내용
        val marketNameIntent = intent.getStringExtra("marketName")        //시장이름
        val storeNameIntent = intent.getStringExtra("storeName")          //가게이름
        val ratingIntent = intent.getDoubleExtra("rating", 0.0)   //별점
        val regionIntent = intent.getStringExtra("region")                  //지역
        val costIntent = intent.getStringExtra("cost")               //물고기종류
        val userIdIntent = intent.getStringExtra("userId")     // 회원id
        val imageIntent = intent.getStringExtra("image")        //이미지
        val menuCostIntent = intent.getStringExtra("menuCost")        //메뉴가격


        //유저
        val nicknameInten = intent.getStringExtra("userNickname")
        val UserimageInten = intent.getStringExtra("userImage")

<<<<<<< HEAD
        //좋아요
        val likeInten = intent.getStringExtra("likefiled")


        //파이어베이스사용
        val db = FirebaseFirestore.getInstance()


                //물고기종류에 따른 가격가져오기
                val fishkindCostIntent = intent.getStringExtra("fishkindcost")
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

=======

        //파이어베이스사용
        val db = FirebaseFirestore.getInstance()


                //물고기종류에 따른 가격가져오기
                val fishkindCostIntent = intent.getStringExtra("fishkindcost")
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

>>>>>>> e6343df945f4d1bf2f683aaffc2611f55bbb3128
                    }


                // 데이터를 TextView에 설정
                val reviewContent = findViewById<TextView>(R.id.contentView)
//        val storeName = findViewById<TextView>(R.id.storeNameBar)
                val rating = findViewById<TextView>(R.id.mStarsocore)
                val userNicName = findViewById<TextView>(R.id.mName)
                val Image = findViewById<ImageView>(R.id.Reviewimg)
                val userimg = findViewById<ImageView>(R.id.mImg)
                val menuCost = findViewById<TextView>(R.id.StorePriceInt)

<<<<<<< HEAD
                //좋아요
                val likeText =  findViewById<TextView>(R.id.likeInt)

=======
>>>>>>> e6343df945f4d1bf2f683aaffc2611f55bbb3128

                //물고기 TextView에 설정
                val fishKindCost = findViewById<TextView>(R.id.MenuFishName)


<<<<<<< HEAD
=======
//                // 좋아요 버튼
//                val likeButton = findViewById<ImageView>(R.id.up)
//                val likeCount = findViewById<TextView>(R.id.likeInt)
//                var isLiked = false



>>>>>>> e6343df945f4d1bf2f683aaffc2611f55bbb3128

                //별모양
                val DratingBar = findViewById<RatingBar>(R.id.DratingBar)
                DratingBar.rating = ratingIntent.toFloat()


                //리뷰
                reviewContent.text = "$reviewContentIntent"
//        storeName.text = "$storeNameIntent"
                rating.text = "$ratingIntent"
                userNicName.text = "$nicknameInten"
                menuCost.text = "$menuCostIntent"
<<<<<<< HEAD
                likeText.text = "$likeInten"
=======
>>>>>>> e6343df945f4d1bf2f683aaffc2611f55bbb3128


                //몰고기 이름
                fishKindCost.text = "$fishkindCostIntent"


                //이미지 설정
                Glide.with(this)
                    .load(imageIntent)
                    .into(Image)

                //이미지 설정(유저)
                Glide.with(this)
                    .load(UserimageInten)
                    .into(userimg)




            }
<<<<<<< HEAD
=======

    //좋아요 기능
//    private fun onLikeButtonClicked(userId: String?, reviewContent: String?) {
//        if(userId != null && reviewContent != null){
//            val db = FirebaseFirestore.getInstance()
//
//            //리뷰내용
//            val reviewRef = db.collection("review").document(reviewContent)
//
//            //좋아요 처리
//            db.runTransaction{ transaction ->
//                val reviewDoc = transaction.get(reviewRef)
//
//                if(reviewDoc.exists()){
//                    val currentLikes = reviewDoc.getLong("likes") ?: 0
//                    transaction.update(reviewRef, "likes", currentLikes + 1)
//                }else{
//                    Log.e("LikeButtonError", "Review document does not exist.")
//                }
//
//                null
//            }.addOnSuccessListener {
//                isLiked = true
//                likeButton.setImageResource(R.drawable.thumb)
//            }.addOnFailureListener { e ->
//                Log.e("LikeButtonError", "Error updating likes: $e")
//            }
//        } else {
//            Log.e("LikeButtonError", "User ID or Review Content is null.")
//        }
//            }

>>>>>>> e6343df945f4d1bf2f683aaffc2611f55bbb3128

}