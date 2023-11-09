package com.example.eatraw

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.eatraw.data.Review
import com.example.eatraw.mypagefrg.MyreviewFragment
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class WriteActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private var selectedImageUri: Uri? = null
    private lateinit var titleSwitch: TextView
    private lateinit var editStoreName: EditText
    private lateinit var editFishKind: EditText
    private lateinit var editFishPrice: EditText
    private lateinit var starSelect: Spinner
    private lateinit var editText: EditText
    private lateinit var btnReview: Button
    private lateinit var btnImage: Button
    private lateinit var editMarketName: EditText
    private lateinit var thumbnailImageView: ImageView
    private var reviewId: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        editStoreName = findViewById(R.id.editStoreName)
        editFishKind = findViewById(R.id.editFishName)
        editFishPrice = findViewById(R.id.editFishPrice)
        starSelect = findViewById(R.id.starSelect)
        editText = findViewById(R.id.editText)
        btnReview = findViewById(R.id.btnReview)
        btnImage = findViewById(R.id.btnImage)
        editMarketName = findViewById(R.id.editMarketName)
        thumbnailImageView = findViewById(R.id.thumbnailImageView)
        titleSwitch = findViewById(R.id.title_switch)

        btnImage.setOnClickListener {
            openImagePicker()
        }

        btnReview.setOnClickListener {
            uploadImageAndAddReviewToFirestore()
        }

        reviewId = intent.getStringExtra("reviewId")
        if (reviewId != null) {
            // 수정 모드인 경우 기존 리뷰 데이터 불러오기
            loadReviewData()
            titleSwitch.text = "리뷰 수정"
            btnReview.text = "수정하기"
        } else {
            // 일반 글쓰기 모드인 경우
            titleSwitch.text = "리뷰 작성"
            btnReview.text = "작성하기"
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun uploadImageAndAddReviewToFirestore() {
        if (selectedImageUri != null) {
            val fishKind = editFishKind.text.toString().trim()
            val costInput = editFishPrice.text.toString().trim()
            if (fishKind.isEmpty()) {
                Toast.makeText(this, "생선 종류를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return
            }
            if (costInput.isEmpty()) {
                Toast.makeText(this, "가격을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return
            }
            if (!costInput.all { it.isDigit() }) {
                Toast.makeText(this, "숫자만 입력 해주세요.", Toast.LENGTH_SHORT).show()
                return
            }

            val imageFileName = selectedImageUri!!.lastPathSegment // 이미지 파일 이름을 원본 파일명으로 설정

            // Image upload
            val imageRef = storageReference.child("storeImg/$imageFileName")
            val uploadTask: UploadTask = imageRef.putFile(selectedImageUri!!)

            val user = Firebase.auth.currentUser
            val userUid = user?.uid

            uploadTask.addOnSuccessListener { taskSnapshot ->
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val content = editText.text.toString()
                    val cost = costInput.toInt()
                    val storeName = editStoreName.text.toString()
                    val selectedRating = starSelect.selectedItem.toString().toFloat()
                    val marketName = editMarketName.text.toString()

                    val reviewData = hashMapOf(
                        "content" to content,
                        "fishKind" to fishKind,
                        "cost" to cost,
                        "storeImg" to uri.toString(), // 이미지 URL을 저장
                        "storeName" to storeName,
                        "rating" to selectedRating,
                        "marketName" to marketName,
                        "userId" to userUid,

                    )
                    if (reviewId != null) {
                        // 수정 모드인 경우
                        db.collection("review").document(reviewId!!)
                            .update(
                                "content", reviewData["content"],
                                "fishKind", reviewData["fishKind"],
                                "cost", reviewData["cost"],
                                "storeImg", reviewData["storeImg"],
                                "storeName", reviewData["storeName"],
                                "rating", reviewData["rating"],
                                "marketName", reviewData["marketName"],
                                "userId", reviewData["userId"]
                            )
                            .addOnSuccessListener {
                                showResultMessage("리뷰가 성공적으로 수정되었습니다.")
                                val intent = Intent(this, MyreviewFragment::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                showResultMessage("리뷰 수정 중 오류가 발생했습니다.")
                            }
                    } else {
                        // 새 리뷰 추가 모드인 경우
                        db.collection("review")
                            .add(reviewData)
                            .addOnSuccessListener { documentReference: DocumentReference ->
                                val newReviewId = documentReference.id
                                showResultMessage("리뷰가 성공적으로 등록되었습니다.")
                                db.collection("review").document(newReviewId)
                                    .update("reviewId", newReviewId)
                                    .addOnSuccessListener {
                                        val intent = Intent(this, ReviewActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                            }
                            .addOnFailureListener { e ->
                                showResultMessage("리뷰 등록 중 오류가 발생했습니다.")
                            }
                    }
                }
            }
        } else {
            showResultMessage("이미지를 선택해주세요.")
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                selectedImageUri = uri
                showResultMessage("이미지 선택 완료")

                // 섬네일 이미지 표시
                thumbnailImageView.visibility = View.VISIBLE
                thumbnailImageView.setImageURI(uri)
            }
        }
    }

    private fun showResultMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    private fun loadReviewData() {
        val docRef = db.collection("review").document(reviewId!!)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val data = documentSnapshot.data // 데이터를 Map으로 가져옵니다.
            if (data != null) {
                val reviewId = documentSnapshot.id
                val content = data["content"] as? String ?: ""
                val marketName = data["marketName"] as? String ?: ""
                val storeImg = data["storeImg"] as? String ?: ""
                val storeName = data["storeName"] as? String ?: ""
                val rating = (data["rating"] as? Double) ?: 0.0
                val region = data["region"] as? String ?: ""
                val like = (data["like"] as? Long)?.toInt() ?: 0
                val fishKind = data["fishKind"] as? String ?: ""
                val cost = (data["cost"] as? Long)?.toInt() ?: 0
                val userId = data["userId"] as? String ?: ""

                val review = Review( content, marketName, storeImg, storeName, rating, region,
                    like, fishKind, cost, userId,reviewId)
                // 이후에는 review 객체를 사용하면 됩니다.

                editStoreName.setText(review.storeName)
                editFishKind.setText(review.fishKind)
                editFishPrice.setText(review.cost.toString())
                starSelect.setSelection(getIndex(starSelect, review.rating.toString()))
                editText.setText(review.content)
                editMarketName.setText(review.marketName)
                Glide.with(this)
                    .load(review.storeImg)
                    .into(thumbnailImageView)
            } else {
                Log.d(TAG, "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with ", exception)
        }
    }
    private fun getIndex(spinner: Spinner, value: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == value) {
                return i
            }
        }
        return 0 // default value
    }
}
