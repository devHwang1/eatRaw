package com.example.eatraw.mypagefrg

import NickFragment.Companion.PICK_IMAGE_REQUEST
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.eatraw.MypageActivity
import com.example.eatraw.R
import com.example.eatraw.databinding.FragmentModifyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class ModifyFragment : Fragment() {
    private lateinit var binding:FragmentModifyBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var textView: TextView
    private lateinit var editText: EditText
    private var originalNickname: String? = null
    private lateinit var password: EditText
    private lateinit var thumbnail: ImageView
    private lateinit var nickcheck: Button
    private lateinit var btnModify: Button

    private var authLogin: Boolean? = null
    private var nickBoolean = false
    private var selectedImageUri: Uri? = null
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            mAuth = FirebaseAuth.getInstance()
            db = FirebaseFirestore.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentModifyBinding.inflate(inflater, container, false)
        val user = mAuth.currentUser
        textView = binding.email
        thumbnail = binding.thumbnail
        editText =binding.nickInput
        password = binding.password
        textView.text = user?.email
        nickcheck = binding.nickcheck
        btnModify = binding.btnModify


        val userId = mAuth.currentUser?.uid
        userId?.let {
            getUserInfo(it)
        }

        binding.root.setOnClickListener { }
        nickcheck.setOnClickListener {
            val nick = editText.text.toString()
            checkNickname(nick)
            if (nick.isEmpty() ) {
                Toast.makeText(context, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else if(!nickBoolean){
                return@setOnClickListener
            }
        }
        btnModify.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val newPassword =  password.text.toString()

            if (newPassword.isEmpty() && authLogin != true) {
                Toast.makeText(context, "Enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!newPassword.isEmpty()) {
                user?.updatePassword(newPassword)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User password updated.")
                        }
                    }
            }

                if(!nickBoolean){
                    Toast.makeText(context, "닉네임을 확인해주세요", Toast.LENGTH_SHORT).show()
                }else{
                    uploadImage()
                }
            }
        thumbnail.setOnClickListener{
            openGallery()
        }




        return binding.root
    }
    private fun getUserInfo(userId: String) {
        val docRef = db.collection("users").document(userId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val thumbnailUrl = document.getString("imageUrl")

                    val nick = document.getString("nickname")
                    authLogin = document.getBoolean("aouthLogin") ?: false
                    password.visibility = if (authLogin!!) View.INVISIBLE else View.VISIBLE

                    if (nick != null) {
                        editText.setText(nick) // 가져온 닉네임을 EditText에 설정
                        originalNickname = nick
                    } else {
                        println("No such nickname")
                    }

                    if (thumbnailUrl != null) {
                        val thumbnailImageView = thumbnail
                        Glide.with(this).load(thumbnailUrl).placeholder(R.drawable.group_40).circleCrop().into(thumbnailImageView)
                    } else {
                        println("No such thumbnailUrl")
                    }

                } else {
                    println("No such document")
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }

    private fun checkNickname(nickname: String) {


        if (nickname == originalNickname) {
            Toast.makeText(context, "닉네임을 변경하지 않았습니다.", Toast.LENGTH_SHORT).show()
            nickBoolean = true
            return
        }

        val usersCollection = FirebaseFirestore.getInstance().collection("users")

        usersCollection.whereEqualTo("nickname", nickname).get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // 닉네임이 중복되지 않음
                    Toast.makeText(context, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show()
                    nickBoolean = true
                } else {
                    // 닉네임이 중복됨
                    Toast.makeText(context, "이미 사용 중인 닉네임입니다.", Toast.LENGTH_SHORT).show()
                    nickBoolean = false
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null) {
                // 선택한 이미지의 URI 가져오기
                selectedImageUri = data.data
                // Glide를 사용하여 이미지 표시
                Glide.with(this).load(selectedImageUri).placeholder(R.drawable.group_40).circleCrop().into(thumbnail)
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
                    Toast.makeText(context, "img upload failed.", Toast.LENGTH_SHORT).show()
                }
            }
        } else if(selectedImageUri == null){
            saveImageUrlToDatabase("")
        }
    }

    private fun saveImageUrlToDatabase(imageUrl: String) {
        val db = FirebaseFirestore.getInstance()
        // 프래그먼트에서는 intent를 직접 받을 수 없으므로 아래와 같이 변경
        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val userRef = db.collection("users").document(currentUser?.uid!!)
        // Firestore에 사용자 정보 저장
        val user  = hashMapOf<String, Any>(
            "nickname" to editText.text.toString(),
            "imageUrl" to imageUrl
        )

        userRef.update(user)
            .addOnSuccessListener { e ->
                Toast.makeText(
                    context,
                    "수정 완료!",
                    Toast.LENGTH_SHORT
                ).show()

                // 데이터베이스 업데이트가 성공한 후 MainActivity로 이동
                val intent = Intent(context, MypageActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context,
                    "Update failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }
    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }
}