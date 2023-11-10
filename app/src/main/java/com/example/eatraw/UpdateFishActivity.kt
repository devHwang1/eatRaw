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
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.storage

class UpdateFishActivity : AppCompatActivity() {

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

    // 기존 물고기 정보를 저장할 변수
    private lateinit var existingFishName: String
    private lateinit var existingFishImg: String
    private var existingMinCost: Long = 0
    private var existingAvgCost: Long = 0
    private var existingMaxCost: Long = 0
    private lateinit var existingSeason: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_fish)

        fishImage = findViewById(R.id.fishImg)
        fishNameEditText = findViewById(R.id.editFishName)
        minCostEditText = findViewById(R.id.editMinCost)
        avgCostEditText = findViewById(R.id.editAvgCost)
        maxCostEditText = findViewById(R.id.editMaxCost)
        seasonEditText = findViewById(R.id.editSeason)

        // Firebase 구성 요소 초기화
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageReference = Firebase.storage.reference

        // Intent에서 기존 물고기 정보 가져오기
        val intent = intent
        if (intent != null && intent.extras != null) {
            val bundle = intent.extras!!
            existingFishName = bundle.getString("fishName", "")
            existingFishImg = bundle.getString("fishImg", "")
            existingMinCost = bundle.getLong("minCost", 0)
            existingAvgCost = bundle.getLong("avgCost", 0)
            existingMaxCost = bundle.getLong("maxCost", 0)
            existingSeason = bundle.getString("season", "")

            // 기존 물고기 정보를 UI에 표시
            displayExistingFishInfo()
        }

        // 이미지 업로드 버튼(uploadButton)을 클릭할 때 이미지를 선택할 수 있도록 클릭 이벤트를 설정합니다.
        uploadButton?.setOnClickListener {
            openImageChooser()
        }

        // "물고기 수정" 버튼 클릭 시
        val btnUpdateFish = findViewById<Button>(R.id.btnUpdateFish)
        btnUpdateFish.setOnClickListener {
            // 사용자로부터 필요한 정보 가져오기
            val fishName = fishNameEditText.text.toString()
            val minCost = minCostEditText.text.toString()
            val avgCost = avgCostEditText.text.toString()
            val maxCost = maxCostEditText.text.toString()
            val season = seasonEditText.text.toString()

            // Firebase Firestore에 데이터 업로드
//            if (fishName.isNotBlank() && minCost.isNotBlank() && avgCost.isNotBlank() && maxCost.isNotBlank() && season.isNotBlank() && imageUri != null) {
                // Firestore에 데이터 업로드
                updateFishData(imageUri!!, fishName, minCost, avgCost, maxCost, season)
//            } else {
//                Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show()
//            }
        }

        // 뒤로가기 버튼 처리
        val backButton = findViewById<ImageView>(R.id.img_backarrow)
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun displayExistingFishInfo() {
        // 기존 물고기 정보를 UI에 표시
        fishNameEditText.setText(existingFishName)
        Glide.with(this).load(existingFishImg).into(fishImage)
        minCostEditText.setText(existingMinCost.toString())
        avgCostEditText.setText(existingAvgCost.toString())
        maxCostEditText.setText(existingMaxCost.toString())
        seasonEditText.setText(existingSeason)
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

    // 물고기 정보 업데이트 함수
    // 업데이트 함수에 기존 정보를 추가하여 업데이트하도록 함
    private fun updateFishData(uri: Uri, fishName: String, minCost: String, avgCost: String, maxCost: String, season: String) {
        // Firebase Storage에 이미지를 저장하기 위한 고유한 이름으로 참조를 생성합니다.
        val imagesRef = storageReference?.child("FishImg/$fishName")
        val uploadTask: UploadTask = imagesRef?.putFile(uri)!!

        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                imagesRef.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri
                    // Firestore에 데이터 업데이트
                    val fishData = hashMapOf(
                        "f_img" to downloadUrl.toString(),
                        "f_name" to fishName,
                        "f_min" to minCost.toLong(),
                        "f_avg" to avgCost.toLong(),
                        "f_max" to maxCost.toLong(),
                        "f_season" to season
                    )

                    // 기존 물고기 정보 업데이트
                    firestore.collection("fish").document(existingFishName)
                        .set(fishData)
                        .addOnSuccessListener {
                            // 업데이트 성공 시 처리
                            Toast.makeText(this, "물고기 정보가 성공적으로 수정되었습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            // 업데이트 실패 시 처리
                            Toast.makeText(this, "물고기 정보 수정 실패: $e", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                // 업로드 실패 처리
                Toast.makeText(this, "이미지 업로드 실패.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
