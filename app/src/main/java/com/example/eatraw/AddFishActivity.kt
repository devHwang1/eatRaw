package com.example.eatraw

import NickFragment.Companion.PICK_IMAGE_REQUEST
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.eatraw.admin.fish.FishFragment
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.storage

class AddFishActivity : AppCompatActivity() {
    private lateinit var fishImage: ImageView
    private var uploadButton: Button? = null
    private lateinit var fishNameEditText: EditText
    private lateinit var minCostEditText: EditText
    private lateinit var avgCostEditText: EditText
    private lateinit var maxCostEditText: EditText
    private lateinit var seasonEditText: EditText
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private var imageUri: Uri? = null
    private var storageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_fish)

        fishImage = findViewById(R.id.fishImg)
        uploadButton = findViewById(R.id.btnUploadImage)
        fishNameEditText = findViewById(R.id.editFishName)
        minCostEditText = findViewById(R.id.editMinCost)
        avgCostEditText = findViewById(R.id.editAvgCost)
        maxCostEditText = findViewById(R.id.editMaxCost)
        seasonEditText = findViewById(R.id.editSeason)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageReference = Firebase.storage.reference

        // 이미지 업로드 버튼(uploadButton)을 클릭할 때 이미지를 선택할 수 있도록 클릭 이벤트를 설정합니다.
        uploadButton?.setOnClickListener {
            openImageChooser()
        }

        // "생선 등록" 버튼 클릭 시
        val btnRegisterFish = findViewById<Button>(R.id.btnRegisterFish)
        btnRegisterFish.setOnClickListener {
            // 사용자로부터 필요한 정보 가져오기
            val fishName = fishNameEditText.text.toString()
            val minCost = minCostEditText.text.toString()
            val avgCost = avgCostEditText.text.toString()
            val maxCost = maxCostEditText.text.toString()
            val season = seasonEditText.text.toString()

            // Firebase Firestore에 데이터 업로드
            if (fishName.isNotBlank() && minCost.isNotBlank() && avgCost.isNotBlank() && maxCost.isNotBlank() && season.isNotBlank() && imageUri != null) {
                // Firestore에 데이터 업로드
                uploadImage(imageUri!!, fishName)
            } else {
                Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 뒤로가기 버튼 처리
        val backButton = findViewById<ImageView>(R.id.img_backarrow)
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "이미지 선택"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            // 사용자가 이미지를 선택하면 이미지의 Uri를 저장합니다.
            imageUri = data.data
            // 이미지를 로드하고 표시
            Glide.with(this)
                .load(imageUri)
                .into(fishImage)
        }
    }

    // 이미지 업로드 함수
    private fun uploadImage(uri: Uri, fishName: String) {
        // Firebase Storage에 이미지를 저장하기 위한 고유한 이름으로 참조를 생성합니다.

        val imagesRef = storageReference?.child("FishImg/$fishName")
        val uploadTask: UploadTask = imagesRef?.putFile(uri)!!

        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                imagesRef.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri
                    // Firestore에 데이터 업로드
                    val fishData = hashMapOf(
                        "f_img" to downloadUrl.toString(),
                        "f_name" to fishName,
                        "f_min" to minCostEditText.text.toString().toLong(),
                        "f_avg" to avgCostEditText.text.toString().toLong(),
                        "f_max" to maxCostEditText.text.toString().toLong(),
                        "f_season" to seasonEditText.text.toString()
                    )


                    firestore.collection("fish")
                        .add(fishData)
                        .addOnSuccessListener { documentReference ->
                            // 업로드 성공 시 처리
                            Toast.makeText(this, "생선 정보가 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show()
                            val fragmentTransaction = supportFragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.fragment_container, FishFragment())
                            fragmentTransaction.commit()
                        }
                        .addOnFailureListener { e ->
                            // 업로드 실패 시 처리
                            Toast.makeText(this, "생선 정보 업로드 실패: $e", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                // 업로드 실패 처리
                Toast.makeText(this, "이미지 업로드 실패.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
