package com.example.eatraw.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatraw.R
import com.example.eatraw.data.Users
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class UsersAdapter(private val context: Context, private val userList: MutableList<Users>) :
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    fun setData(newUsersList: List<Users>) {
        userList.clear()
        userList.addAll(newUsersList)
        Log.d("UsersAdapter", "Data set: $userList")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_users_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val emailTextView: TextView = itemView.findViewById(R.id.email)
        private val nicknameTextView: TextView = itemView.findViewById(R.id.userNickName)
        private val aouthLoginTextView: TextView = itemView.findViewById(R.id.userAouthLogin)
        private val adminTextView: TextView = itemView.findViewById(R.id.userAdmin)
        private val userImageView: ImageView = itemView.findViewById(R.id.userImg)
        private val btnDeleteUser: Button = itemView.findViewById(R.id.btnDeleteUser)
        private val btnUpdateUserAdmin: Button = itemView.findViewById(R.id.btnUpdateUserAdmin)
        private val likeMarketTextView: TextView = itemView.findViewById(R.id.likeMarket)


        fun bind(user: Users) {
            emailTextView.text = user.email
            nicknameTextView.text = user.nickname
            aouthLoginTextView.text = if (user.aouthLogin) "OAuth 사용 중" else "OAuth 미사용"
            adminTextView.text = if (user.admin) "관리자" else "일반 사용자"
            likeMarketTextView.text = user.likeMarket ?: "없음"

            // 이미지 로드 (Glide 사용 예제)
            Glide.with(context)
                .load(user.imageUrl)
                .placeholder(R.drawable.default_nallo)
                .error(R.drawable.default_nallo)
                .into(userImageView)

            btnDeleteUser.setOnClickListener {
                val userToDelete = userList[adapterPosition]
                deleteUser(userToDelete)
            }

            btnUpdateUserAdmin.setOnClickListener {
                // 클릭 이벤트 처리
                val userToUpdate = userList[adapterPosition]

                // 파이어베이스 업데이트
                val db = FirebaseFirestore.getInstance()
                val collectionReference = db.collection("users")

                val documentId: String? = getDocumentIdForUser(userToUpdate)

                if (documentId != null) {
                    // Firestore에서 현재 "admin" 필드 값을 가져옴
                    collectionReference.document(documentId)
                        .get()
                        .addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                val currentAdminValue = documentSnapshot.getBoolean("admin") ?: false

                                // "admin" 필드 값을 반대로 업데이트
                                collectionReference.document(documentId)
                                    .update("admin", !currentAdminValue)
                                    .addOnSuccessListener {
                                        // 업데이트 성공 시 추가 작업 수행
                                        userToUpdate.admin = !currentAdminValue // userList에서 해당 사용자의 "admin" 값을 업데이트
                                        notifyDataSetChanged() // RecyclerView를 갱신하여 변경 사항을 표시
                                    }
                                    .addOnFailureListener { e ->
                                        // 업데이트 실패 시 처리
                                        Log.e("FirestoreError", "Error updating user: ", e)
                                    }
                            }
                        }
                }

            }
        }

        private fun deleteUser(user: Users) {
            val db = FirebaseFirestore.getInstance()
            val collectionReference = db.collection("users")

            val documentId: String? = getDocumentIdForUser(user)

            if (documentId != null) {
                collectionReference.document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        // 삭제 성공 시, 데이터 목록에서도 제거
                        val deletedIndex = userList.indexOfFirst { it.email == user.email }
                        if (deletedIndex != -1) {
                            userList.removeAt(deletedIndex)
                            notifyItemRemoved(deletedIndex)
                        }
                    }
                    .addOnFailureListener { e ->
                        // 삭제 실패 시 처리
                        Log.e("FirestoreError", "Error deleting user: ", e)
                    }
            }
        }

        private fun getDocumentIdForUser(user: Users): String? = runBlocking {
            val db = FirebaseFirestore.getInstance()
            val collectionReference = db.collection("users")

            var documentId: String? = null

            try {
                val querySnapshot = collectionReference
                    .whereEqualTo("email", user.email)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    documentId = document.id
                    break
                }
            } catch (exception: Exception) {
                // 오류 처리
                exception.printStackTrace()
            }

            documentId
        }
    }
}