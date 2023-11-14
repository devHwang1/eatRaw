package com.example.eatraw.adapter
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatraw.MypageActivity
import com.example.eatraw.R
import com.example.eatraw.WriteActivity
import com.example.eatraw.data.Review
import com.example.eatraw.data.Users
import com.google.firebase.firestore.FirebaseFirestore


class MyReviewAdapter(val context: Context,private val review: List<Review>, private val users: List<Users>) :
    RecyclerView.Adapter<MyReviewAdapter.MyReviewAdapterViewHolder>() {

    class MyReviewAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNickname: TextView = itemView.findViewById(R.id.mName)
        val userImage: ImageView = itemView.findViewById(R.id.mImg)
        val rating: TextView = itemView.findViewById(R.id.mStarsocore)
        val fishKind: TextView = itemView.findViewById(R.id.MenuFishName)
        val cost: TextView = itemView.findViewById(R.id. StorePriceInt)
        val Image: ImageView = itemView.findViewById(R.id.Reviewimg)
        val fishMinText: TextView = itemView.findViewById(R.id.IntMin)
        val fishAvgText: TextView = itemView.findViewById(R.id.IntAvg)
        val fishMaxText: TextView = itemView.findViewById(R.id.IntMax)
        val DratingBar: RatingBar = itemView.findViewById(R.id.DratingBar)
        val dropdetailImageView: ImageView = itemView.findViewById(R.id.drop_detail)
        val reviewContent : TextView = itemView.findViewById(R.id.contentView)
        val topLayout: View = itemView.findViewById(R.id.topLayout)
        val bnv_m: View = itemView.findViewById(R.id.bnv_m)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReviewAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_detail_box, parent, false)
        return MyReviewAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyReviewAdapterViewHolder, position: Int) {
        val currentReview = review[position]
        val user = users.find { it.userId == currentReview.userId }
        val db = FirebaseFirestore.getInstance()

        holder.dropdetailImageView.visibility = View.VISIBLE
        holder.topLayout.visibility = View.GONE
        holder.bnv_m.visibility = View.GONE
        holder.dropdetailImageView.setOnClickListener {
            currentReview?.let{
                Log.d("MyReviewAdapter", "Review ID: ${currentReview.reviewId}")
                // 팝업 메뉴 생성
                val popup = PopupMenu(holder.itemView.context, holder.dropdetailImageView)
                // 팝업 메뉴에 메뉴 아이템 추가
                popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)
                // 팝업 메뉴의 메뉴 아이템 클릭 리스너 설정
                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_edit -> {
                            editReview(currentReview, holder.itemView.context)
                            true
                        }
                        R.id.action_delete -> {
                            deleteReview(currentReview)
                            true
                        }
                        else -> false
                    }
                }
                // 팝업 메뉴 보여주기
                popup.show()
            }



        }

        holder.apply {
            userNickname.text = user?.nickname
            user?.imageUrl?.let { imageUrl ->
                Glide.with(itemView)
                    .load(imageUrl)
                    .into(userImage)
            }

            rating.text = currentReview.rating.toString()
            fishKind.text = currentReview.fishKind
            cost.text = currentReview.cost.toString()
            reviewContent.text = currentReview.content
            Glide.with(itemView)
                .load(currentReview.storeImg)
                .into(Image)
            DratingBar.rating = currentReview.rating!!.toFloat()

            db.collection("fish")
                .whereEqualTo("f_name", fishKind.text.toString())
                .get()
                .addOnSuccessListener { fishDocuments ->
                    if (!fishDocuments.isEmpty) {
                        val fishDocument = fishDocuments.documents[0]
                        val fishMin = fishDocument.getLong("f_min")
                        val fishAvg = fishDocument.getLong("f_avg")
                        val fishMax = fishDocument.getLong("f_max")

                        fishMinText.text = fishMin.toString()
                        fishAvgText.text = fishAvg.toString()
                        fishMaxText.text = fishMax.toString()
                    } else {
                        Log.e("FirestoreError", "Error getting fish document: No matching documents.")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("FirestoreError", "Error getting fish document: ", exception)
                }
        }
    }

    override fun getItemCount(): Int {
        println("Loaded ${review.size} reviews")
        return review.size
    }
    // 리뷰를 수정하는 함수
    private fun editReview(review: Review, context: Context) {
        val intent = Intent(context, WriteActivity::class.java)
        intent.putExtra("reviewId", review.reviewId)
        context.startActivity(intent)
    }

    // 리뷰를 삭제하는 함수
    private fun deleteReview(review: Review) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("삭제 확인")
        alertDialogBuilder.setMessage("정말로 이 리뷰를 삭제하시겠습니까?")

        alertDialogBuilder.setPositiveButton("삭제") { _, _ ->
            // "삭제" 버튼이 클릭되었을 때의 동작
            deleteReviewConfirmed(review)
        }

        alertDialogBuilder.setNegativeButton("취소") { dialog, _ ->
            // "취소" 버튼이 클릭되었을 때의 동작
            dialog.dismiss() // 다이얼로그를 닫습니다.
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
    private fun deleteReviewConfirmed(review: Review) {
        val db = FirebaseFirestore.getInstance()
        db.collection("review").document(review.reviewId!!)
            .delete()
            .addOnSuccessListener {
                // 삭제 성공 시 수행할 동작
                Log.d("MyReviewAdapter", "Review deleted successfully")
                val intent = Intent(context, MypageActivity::class.java)
                context.startActivity(intent)
            }
            .addOnFailureListener { e ->
                // 삭제 실패 시 수행할 동작
                Log.w("MyReviewAdapter", "Error deleting review", e)
            }
    }
}