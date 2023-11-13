package com.example.eatraw.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatraw.DetailActivity
import com.example.eatraw.R
import com.example.eatraw.data.Review
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class ReviewAdminAdapter(private val context: Context, private val reviewList: MutableList<Review>) :
    RecyclerView.Adapter<ReviewAdminAdapter.ViewHolder>() {

    // setData 메서드 추가
    fun setData(newList: List<Review>) {
        reviewList.clear()
        reviewList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review_admin_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviewList[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val reviewImage: ImageView = itemView.findViewById(R.id.reviewImageAdmin)
        private val reviewStoreName: TextView = itemView.findViewById(R.id.reviewStoreNameAdmin)
        private val reviewContent: TextView = itemView.findViewById(R.id.reviewContentAdmin)
        private val rating: TextView = itemView.findViewById(R.id.ratingAdmin)
        private val hashtag: TextView = itemView.findViewById(R.id.button1Admin)
        private val btnDeleteReviewAdmin: Button = itemView.findViewById(R.id.btnDeleteReviewAdmin)

        fun bind(review: Review) {
            // 아이템 뷰에 데이터를 바인딩하는 코드 작성
            reviewStoreName.text = review.storeName
            reviewContent.text = review.content
            rating.text = review.rating.toString()
            hashtag.text = review.marketName
            // 이미지 로딩 (Glide 사용 예제)
            Glide.with(context)
                .load(review.storeImg)
                .placeholder(R.drawable.default_nallo)
                .error(R.drawable.default_nallo)
                .into(reviewImage)

            btnDeleteReviewAdmin.setOnClickListener {
                val reviewToDelete = reviewList[adapterPosition]
                deleteReview(reviewToDelete)
            }

            itemView.setOnClickListener {
                val review = reviewList[adapterPosition]

                // Intent를 사용하여 DetailActivity로 이동
                val intent = Intent(context, DetailActivity::class.java)

                // 리뷰 정보를 Intent에 추가
                intent.putExtra("reviewContent", review.content)
                intent.putExtra("marketName", review.marketName)
                intent.putExtra("storeName", review.storeName)
                intent.putExtra("rating", review.rating)
                intent.putExtra("region", review.region)
                intent.putExtra("cost", review.cost)
                intent.putExtra("userId", review.userId)
                intent.putExtra("image", review.storeImg)
                intent.putExtra("fishkindcost", review.fishKind)

                // 유저 정보 추가
//                intent.putExtra("userNickname", review.userNickname)
//                intent.putExtra("userImage", review.userImage)

                // 다른 정보도 필요한 경우 여기에 추가

                context.startActivity(intent)
            }

        }

        private fun deleteReview(review: Review) {
            val db = FirebaseFirestore.getInstance()
            val collectionReference = db.collection("review")

            val documentId: String? = getDocumentIdForReview(review)

            if (documentId != null) {
                collectionReference.document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        // 삭제 성공 시, 데이터 목록에서도 제거
                        val deletedIndex = reviewList.indexOfFirst { it.content == review.content }
                        if (deletedIndex != -1) {
                            reviewList.toMutableList().removeAt(deletedIndex)
                            notifyItemRemoved(deletedIndex)
                        }
                    }
                    .addOnFailureListener { e ->
                        // 삭제 실패 시 처리
                        Log.e("FirestoreError", "Error deleting review: ", e)
                    }
            }
        }

        private fun getDocumentIdForReview(review: Review): String? = runBlocking {
            val db = FirebaseFirestore.getInstance()
            val collectionReference = db.collection("review")

            var documentId: String? = null

            try {
                val querySnapshot = collectionReference
                    .whereEqualTo("content", review.content)
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
