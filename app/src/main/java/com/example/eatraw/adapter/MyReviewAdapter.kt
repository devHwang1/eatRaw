package com.example.eatraw.adapter
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
import com.example.eatraw.R
import com.example.eatraw.WriteActivity
import com.example.eatraw.data.Review
import com.example.eatraw.data.Users

class MyReviewAdapter(private val review: List<Review>, private val users: List<Users>) :
    RecyclerView.Adapter<MyReviewAdapter.MyReviewAdapterViewHolder>() {

    class MyReviewAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNickname: TextView = itemView.findViewById(R.id.mName)
        val userImage: ImageView = itemView.findViewById(R.id.mImg)
        val rating: TextView = itemView.findViewById(R.id.mStarsocore)
        val fishKind: TextView = itemView.findViewById(R.id.MenuFishName)
        val cost: TextView = itemView.findViewById(R.id. StorePriceInt)
        val Image: ImageView = itemView.findViewById(R.id.Reviewimg)
        val userimg: ImageView = itemView.findViewById(R.id.mImg)
        val fishMinText: TextView = itemView.findViewById(R.id.IntMin)
        val fishAvgText: TextView = itemView.findViewById(R.id.IntAvg)
        val fishMaxText: TextView = itemView.findViewById(R.id.IntMax)
        val DratingBar: RatingBar = itemView.findViewById(R.id.DratingBar)
        val dropdetailImageView: ImageView = itemView.findViewById(R.id.drop_detail)
        val topLayout: View = itemView.findViewById(R.id.topLayout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReviewAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_detail_box, parent, false)
        return MyReviewAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyReviewAdapterViewHolder, position: Int) {
        val currentReview = review[position]
        val user = users.find { it.userId == currentReview.userId }


        holder.dropdetailImageView.visibility = View.VISIBLE
        holder.dropdetailImageView.setOnClickListener {
            currentReview?.let{

            }
            Log.d("MyReviewAdapter", "Review ID: ${currentReview.reviewId}")
            // 팝업 메뉴 생성
            val popup = PopupMenu(holder.itemView.context, it)
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
            Glide.with(itemView)
                .load(currentReview.storeImg)
                .into(Image)
            // fishMinText.text = /* 물고기 최소 크기 */
            // fishAvgText.text = /* 물고기 평균 크기 */
            // fishMaxText.text = /* 물고기 최대 크기 */
            // DratingBar.rating = currentReview.rating.toFloat()
        }
        holder.topLayout.visibility = View.GONE
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

    }
}
