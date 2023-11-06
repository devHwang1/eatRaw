package com.example.eatraw

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.common.io.Files.getFileExtension
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class WriteActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private var selectedImageUri: Uri? = null

    private lateinit var editStoreName: EditText
    private lateinit var editFishKind: EditText
    private lateinit var editFishPrice: EditText
    private lateinit var starSelect: Spinner
    private lateinit var editText: EditText
    private lateinit var btnReview: Button
    private lateinit var btnImage: Button
    private lateinit var marketName: EditText
    private lateinit var thumbnailImageView: ImageView

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
        marketName = findViewById(R.id.marketName)
        thumbnailImageView = findViewById(R.id.thumbnailImageView)

        btnImage.setOnClickListener {
            openImagePicker()
        }

        btnReview.setOnClickListener {
            uploadImageAndAddReviewToFirestore()
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
            val imageFileName = selectedImageUri!!.lastPathSegment // 이미지 파일 이름을 원본 파일명으로 설정

            // Image upload
            val imageRef = storageReference.child("storeImg/$imageFileName")
            val uploadTask: UploadTask = imageRef.putFile(selectedImageUri!!)

            val user = Firebase.auth.currentUser
            val userUid = user?.uid

            uploadTask.addOnSuccessListener { taskSnapshot ->
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val content = editText.text.toString()
                    val fishKind = editFishKind.text.toString()
                    val cost = editFishPrice.text.toString().toInt()
                    val storeName = editStoreName.text.toString()
                    val selectedRating = starSelect.selectedItem.toString().toFloat()
                    val marketName = marketName.text.toString()

                    val reviewData = hashMapOf(
                        "content" to content,
                        "fishKind" to fishKind,
                        "cost" to cost,
                        "storeImg" to uri.toString(), // 이미지 URL을 저장
                        "storeName" to storeName,
                        "rating" to selectedRating,
                        "marketName" to marketName,
                        "userId" to userUid
                    )

                    db.collection("review")
                        .add(reviewData)
                        .addOnSuccessListener { documentReference: DocumentReference ->
                            val reviewId = documentReference.id
                            showResultMessage("리뷰가 성공적으로 등록되었습니다.")
                            val intent = Intent(this, ReviewActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            showResultMessage("리뷰 등록 중 오류가 발생했습니다.")
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
}
