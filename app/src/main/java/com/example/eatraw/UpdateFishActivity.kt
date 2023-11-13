package com.example.eatraw

import NickFragment.Companion.PICK_IMAGE_REQUEST
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
        uploadButton = findViewById(R.id.btnUploadImage)
        fishNameEditText = findViewById(R.id.editFishName)
        minCostEditText = findViewById(R.id.editMinCost)
        avgCostEditText = findViewById(R.id.editAvgCost)
        maxCostEditText = findViewById(R.id.editMaxCost)
        seasonEditText = findViewById(R.id.editSeason)

        // Firebase 구성 요소 초기화
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageReference = Firebase.storage.reference

        // "이미지 업로드" 버튼 클릭 시
        uploadButton?.setOnClickListener {
            openImageChooser()
        }

        // UpdateFishActivity의 onCreate 함수 내에서 데이터 받아오기
        val intent = intent
        if (intent != null && intent.extras != null) {
            existingFishName = intent.getStringExtra("fishName") ?: ""
            existingFishImg = intent.getStringExtra("fishImg") ?: ""
            existingMinCost = intent.getLongExtra("minCost", 0)
            existingAvgCost = intent.getLongExtra("avgCost", 0)
            existingMaxCost = intent.getLongExtra("maxCost", 0)
            existingSeason = intent.getStringExtra("season") ?: ""

            // 문서 ID를 얻음
            val documentId = intent.getStringExtra("documentId") ?: ""

            // documentData를 받아옴
            val documentData = intent.getSerializableExtra("fishDocumentData") as Map<String, Any>?

            // 기존 물고기 정보를 UI에 표시
            displayExistingFishInfo(documentData)

            // documentId를 사용할 수 있습니다.
            Log.d("DocumentId", "Document ID: $documentId")
        }

        // 생선 이미지 업로드 버튼에 대한 클릭 이벤트 핸들러
        val btnUploadImage = findViewById<Button>(R.id.btnUploadImage)
        btnUploadImage.setOnClickListener {
            openImageChooser()
        }

        // "물고기 수정" 버튼 클릭 시
        val btnUpdateFish = findViewById<Button>(R.id.btnUpdateFish)
        btnUpdateFish.setOnClickListener {
            // 사용자로부터 필요한 정보 가져오기
            val fishName = fishNameEditText.text.toString()

            if (fishName.isNotBlank()) {
                val minCost = minCostEditText.text.toString().toLong()
                val avgCost = avgCostEditText.text.toString().toLong()
                val maxCost = maxCostEditText.text.toString().toLong()
                val season = seasonEditText.text.toString()

                // Firebase Firestore에 데이터 업로드
                if (imageUri != null) {
                    updateFishData(imageUri!!, fishName, minCost, avgCost, maxCost, season)
                } else {
                    Toast.makeText(this, "이미지를 선택하세요.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "이름을 넣어주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 뒤로가기 버튼 처리
        val backButton = findViewById<ImageView>(R.id.img_backarrow)
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    // displayExistingFishInfo 함수 내에서 documentData를 사용하여 UI에 표시
    private fun displayExistingFishInfo(documentData: Map<String, Any>?) {
        // 기존 물고기 정보를 UI에 표시
        fishNameEditText.setText(existingFishName)
        Glide.with(this).load(existingFishImg).into(fishImage)
        minCostEditText.setText(existingMinCost.toString())
        avgCostEditText.setText(existingAvgCost.toString())
        maxCostEditText.setText(existingMaxCost.toString())
        seasonEditText.setText(existingSeason)

        // documentData를 사용하여 추가적인 정보를 UI에 표시 (예: 어떤 필드의 데이터를 사용할지에 따라 적절하게 수정)
        if (documentData != null) {
            val additionalData = documentData["additionalField"] as String?
            // 추가적인 정보를 UI에 표시하는 코드 추가
        }
    }

    // 이미지를 선택하는 메소드
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
    private fun updateFishData(
        uri: Uri?,
        fishName: String,
        minCost: Long,
        avgCost: Long,
        maxCost: Long,
        season: String
    ) {
        val imagesRef = storageReference?.child("FishImg/$fishName")
        val uploadTask: UploadTask = uri?.let { imagesRef?.putFile(it) }!!

        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (imagesRef != null) {
                    imagesRef.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri
                        val updatedData = hashMapOf(
                            "f_img" to downloadUrl.toString(),
                            "f_name" to fishName,
                            "f_min" to minCost,
                            "f_avg" to avgCost,
                            "f_max" to maxCost,
                            "f_season" to season
                        )

                        // 수정된 코드: 직접 문서 ID를 사용
                        val documentId = intent.getStringExtra("documentId") ?: ""
                        Log.e("documentId123>>", "$documentId")
                        val existingDocumentRef = firestore.collection("fish").document(documentId)

                        existingDocumentRef.get().addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                existingDocumentRef.update(updatedData as Map<String, Any>)
                                    .addOnSuccessListener {
                                        // 업데이트 성공
                                        Toast.makeText(
                                            this,
                                            "물고기 정보가 성공적으로 수정되었습니다.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("FirestoreError", "Error updating document: $e")
                                        Toast.makeText(this, "물고기 정보 수정 실패: $e", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                            } else {
                                Toast.makeText(this, "물고기 정보 갱신에 실패했습니다.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
        }
    }
}