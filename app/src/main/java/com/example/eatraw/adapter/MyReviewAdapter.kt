package com.example.eatraw.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatraw.R
import com.example.eatraw.data.Review
import com.example.eatraw.data.Users
class MyReviewAdapter(private val review: List<Review>, private val users: List<Users>) :
    RecyclerView.Adapter<MyReviewAdapter.MyReviewAdapterViewHolder>() {

    class MyReviewAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNickname: TextView = itemView.findViewById(R.id.mName)
        val userImage: ImageView = itemView.findViewById(R.id.mImg)
        val rating: TextView = itemView.findViewById(R.id.mStarsocore)
        val fishKind: TextView = itemView.findViewById(R.id.StorePriceInt)
        val cost: TextView = itemView.findViewById(R.id.MenuFishName)
        val Image: ImageView = itemView.findViewById(R.id.Reviewimg)
        val userimg: ImageView = itemView.findViewById(R.id.mImg)
        val fishMinText: TextView = itemView.findViewById(R.id.IntMin)
        val fishAvgText: TextView = itemView.findViewById(R.id.IntAvg)
        val fishMaxText: TextView = itemView.findViewById(R.id.IntMax)
        val DratingBar: RatingBar = itemView.findViewById(R.id.DratingBar)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReviewAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_detail_box, parent, false)
        return MyReviewAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyReviewAdapterViewHolder, position: Int) {
        val currentReview = review[position]
        val user = users.find { it.userId == currentReview.userId }

        holder.apply {
            userNickname.text = user?.nickname
            user?.imageUrl?.let { imageUrl ->
                Glide.with(itemView)
                    .load(imageUrl)
                    .into(userImage)
            }

            rating.text = currentReview.rating.toString()
            fishKind.text = currentReview.fishKind.toString()
            cost.text = currentReview.cost
            Glide.with(itemView)
                .load(currentReview.storeImg)
                .into(Image)
            // fishMinText.text = /* 물고기 최소 크기 */
            // fishAvgText.text = /* 물고기 평균 크기 */
            // fishMaxText.text = /* 물고기 최대 크기 */
            // DratingBar.rating = currentReview.rating.toFloat()
        }
    }

    override fun getItemCount(): Int {
        println("Loaded ${review.size} reviews")
        return review.size
    }
}
