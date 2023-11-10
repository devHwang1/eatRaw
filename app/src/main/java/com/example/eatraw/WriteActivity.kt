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
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

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
    private var loadedImageUrl: String? = null

    @ServerTimestamp
    private var timestamp: Date? = null
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

            titleSwitch.text = "리뷰 수정"
            btnReview.text = "수정하기"
            loadReviewData()

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

        // 'uploadImageAndAddReviewToFirestore' 함수 외부에 추가된 함수
        fun updateFishAvg(fishKind: String) {
            // 'review' 컬렉션에서 'fishKind'가 일치하는 모든 문서를 조회
            db.collection("review")
                .whereEqualTo("fishKind", fishKind)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    var totalCost = 0
                    var reviewCount = 0

                    // 검색된 문서를 반복하며 'cost' 값을 합산
                    for (document in querySnapshot) {
                        val cost = document.getLong("cost")?.toInt() ?: 0
                        totalCost += cost
                        reviewCount++
                    }

                    if (reviewCount > 0) {
                        // 평균값 계산 및 'fish' 문서 업데이트
                        val avgCost = totalCost.toLong() / reviewCount.toLong()
                        db.collection("fish")
                            .whereEqualTo("f_name", fishKind)
                            .get()
                            .addOnSuccessListener { fishSnapshot ->
                                if (!fishSnapshot.isEmpty) {
                                    val fishDoc = fishSnapshot.documents[0]
                                    val newData = HashMap<String, Any>()
                                    newData["f_avg"] = avgCost
                                    newData["f_count"] = reviewCount
                                    db.collection("fish")
                                        .document(fishDoc.id)
                                        .update(newData)
                                        .addOnSuccessListener {
                                            // 'fish' 문서 업데이트 성공 시 처리
                                            // (아래의 코드는 필요에 따라 수정하셔야 합니다.)
                                            showResultMessage("fish 업데이트가 성공했습니다.")
                                        }
                                        .addOnFailureListener { e ->
                                            // 'fish' 문서 업데이트 실패 시 처리
                                            showResultMessage("fish 업데이트 중 오류가 발생했습니다.")
                                        }
                                }
                            }
                    }
                }
                .addOnFailureListener { e ->
                    // 조회 실패 시 처리
                    showResultMessage("review 조회 중 오류가 발생했습니다.")
                }
        }
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

        val content = editText.text.toString()
        val cost = costInput.toInt()
        val storeName = editStoreName.text.toString()
        val selectedRating = starSelect.selectedItem.toString().toFloat()
        val marketName = editMarketName.text.toString()

        val reviewData = hashMapOf(
            "content" to content,
            "fishKind" to fishKind,
            "cost" to cost,
            "storeName" to storeName,
            "rating" to selectedRating,
            "marketName" to marketName,
            "userId" to Firebase.auth.currentUser?.uid,
            "timestamp" to Timestamp.now()
        )

        if (selectedImageUri != null) {
            // 새 이미지를 선택한 경우, 이미지를 업로드한다.
            val imageFileName = selectedImageUri!!.lastPathSegment // 이미지 파일 이름을 원본 파일명으로 설정

            // Image upload
            val imageRef = storageReference.child("storeImg/$imageFileName")
            val uploadTask: UploadTask = imageRef.putFile(selectedImageUri!!)

            uploadTask.addOnSuccessListener { taskSnapshot ->
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    // 이미지 업로드가 성공하면, 새로운 URL을 사용하여 리뷰를 업데이트한다.
                    reviewData["storeImg"] = uri.toString() // 이미지 URL을 저장

                    // 리뷰 업데이트
                    if (reviewId != null) {
                        // 수정 모드인 경우
                        db.collection("review").document(reviewId!!)
                            .update(reviewData as Map<String, Any>)
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
                            .add(reviewData as Map<String, Any>)
                            .addOnSuccessListener { documentReference: DocumentReference ->
                                val newReviewId = documentReference.id
                                showResultMessage("리뷰가 성공적으로 등록되었습니다.")
                                // fish 컬렉션에서 fishKind와 동일한 문서를 조회합니다.
                                val fishKind = editFishKind.text.toString().trim()
                                val cost = editFishPrice.text.toString().toInt() // 사용자로부터 입력받은 cost

                                db.collection("fish")
                                    .whereEqualTo("f_name", fishKind)
                                    .get()
                                    .addOnSuccessListener { querySnapshot ->
                                        if (!querySnapshot.isEmpty) {
                                            val fishDoc = querySnapshot.documents[0]
                                            val f_max = fishDoc.getLong("f_max") ?: 0
                                            val f_min = fishDoc.getLong("f_min") ?: 0
                                            val f_avg = fishDoc.getDouble("f_avg") ?: 0.0
                                            val f_count = fishDoc.getLong("f_count") ?: 0

                                            // cost와 f_max, f_min을 비교하여 업데이트
                                            val newData = HashMap<String, Any>()
                                            if (cost > f_max) {
                                                newData["f_max"] = cost
                                            }
                                            if (cost < f_min || f_min.toInt() == 0) {
                                                newData["f_min"] = cost
                                            }

                                            // fishKind와 동일한 문서 업데이트
                                            db.collection("fish")
                                                .document(fishDoc.id)
                                                .update(newData)
                                                .addOnSuccessListener {
                                                    // 업데이트가 성공한 경우
                                                    // 여기에서 리뷰를 추가하는 나머지 코드를 실행할 수 있습니다.
                                                    // (아래의 코드는 필요에 따라 수정하셔야 합니다.)
                                                    updateFishAvg(fishKind)
                                                }
                                                .addOnFailureListener { e ->
                                                    // 업데이트 실패 시 처리
                                                    showResultMessage("fish 업데이트 중 오류가 발생했습니다.")
                                                }
                                        } else {
                                            // fishKind와 동일한 문서가 없는 경우
                                            // 여기에서 리뷰를 추가하는 나눈지 코드를 실행할 수 있습니다.
                                            // (아래의 코드는 필요에 따라 수정하셔야 합니다.)
                                            updateFishAvg(fishKind)
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        // 조회 실패 시 처리
                                        showResultMessage("fish 조회 중 오류가 발생했습니다.")
                                    }


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
            }.addOnFailureListener { e ->
                // 이미지 업로드에 실패한 경우 오류 메시지를 표시한다.
                showResultMessage("이미지 업로드에 실패했습니다.")
            }
        } else {
            // 이미지를 선택하지 않은 경우, 리뷰만 업데이트한다.
            // 리뷰 업데이트
            if (reviewId != null) {
                // 수정 모드인 경우
                db.collection("review").document(reviewId!!)
                    .update(reviewData as Map<String, Any>)
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
                    .add(reviewData as Map<String, Any>)
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

                if (storeImg.isNotEmpty()) {
                    loadedImageUrl = storeImg
                }

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

                Log.d(TAG, "Image URL: ${review.storeImg}")

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