package com.example.eatraw

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.eatraw.databinding.ActivityNicknameBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class NicknameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNicknameBinding
    private lateinit var nickname: TextInputEditText
    private lateinit var thumbnail: ImageView
    private lateinit var btnRegister: Button
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    private var selectedImageUri: Uri? = null



    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityNicknameBinding.inflate(layoutInflater)
        setContentView(binding.root)


        nickname = binding.nickname
        thumbnail = binding.thumbnail
        btnRegister = binding.btnRegister

        thumbnail.setOnClickListener{
            openGallery()

        }

        btnRegister.setOnClickListener {
            val nick = nickname.text.toString()
            if (nick.isEmpty()) {
                Toast.makeText(this@NicknameActivity, "Enter Nickname", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            uploadImage()

        }

    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                // 선택한 이미지의 URI 가져오기
                selectedImageUri = data.data

                // Glide를 사용하여 이미지 표시
                Glide.with(this).load(selectedImageUri).circleCrop().into(thumbnail)
            }
        }
    }

    private fun uploadImage() {
        if (selectedImageUri != null) {
            val imageRef = storageRef.child("images/${UUID.randomUUID()}")

            // 이미지 업로드
            val uploadTask = imageRef.putFile(selectedImageUri!!)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUrl = task.result
                    saveImageUrlToDatabase(downloadUrl.toString())
                } else {
                    Toast.makeText(this@NicknameActivity, "img upload failed.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this@NicknameActivity, "No image selected.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun saveImageUrlToDatabase(imageUrl: String) {
        val db = FirebaseFirestore.getInstance()
        val email = intent.getStringExtra("email")
        // 사용자 정보 생성
        val userRef = db.collection("users").document(email!!)

        // Firestore에 사용자 정보 저장
        val user  = hashMapOf<String, Any>(
            "nickname" to nickname.text.toString(),
            "imageUrl" to imageUrl
        )

        userRef.set(user)
            .addOnSuccessListener { e ->
                Toast.makeText(
                    this@NicknameActivity,
                    "환영합니다!",
                    Toast.LENGTH_SHORT
                ).show()

                // 데이터베이스 업데이트가 성공한 후 MainActivity로 이동
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@NicknameActivity,
                    "Update failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

}