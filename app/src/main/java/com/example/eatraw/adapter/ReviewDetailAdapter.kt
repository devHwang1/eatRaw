package com.example.eatraw.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatraw.R
import com.example.eatraw.data.Review
import com.example.eatraw.data.Users
import com.google.firebase.auth.FirebaseAuth


//사용 데이터 : Review
open class ReviewDetailAdapter(private var review : List<Review>, var users:List<Users>) :
    RecyclerView.Adapter<ReviewDetailAdapter.ReviewDetailAdapterViewHolder>() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    open class ReviewDetailAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView : ImageView = itemView.findViewById(R.id.Reviewimg)
        val menuFishName : TextView = itemView.findViewById(R.id.MenuFishName)
        val fishPrice : TextView = itemView.findViewById(R.id.StorePriceInt)
        val starScore: TextView = itemView.findViewById(R.id.mStarsocore)
        val ratingBar : RatingBar = itemView.findViewById(R.id.DratingBar)
        val reviewContent : TextView = itemView.findViewById(R.id.contentView)
//        val Textcomparison : TextView = itemView.findViewById(R.id.Textcomparison)


        //user
        val memberName : TextView = itemView.findViewById(R.id.mName)
        val meberProfil: ImageView = itemView.findViewById(R.id.mImg)
//
//        //가격
//        val minPrice : TextView = itemView.findViewById(R.id.IntMin)
//        val maxPrice : TextView = itemView.findViewById(R.id.IntMax)
//        val avgPrice : TextView = itemView.findViewById(R.id.IntAvg)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): ReviewDetailAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_detail_box, parent, false)
        return ReviewDetailAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewDetailAdapterViewHolder, position: Int) {
        val currentReview = review[position]
        val user = users.find { it.userId == currentReview.userId }
//        val reviewDetail = detailreview[position]

        // 리뷰 데이터를 뷰에 연결
        // 이미지 URL을 Glide로 로드
        currentReview.storeImg?.let {
            Glide.with(holder.itemView)
                .load(it)
                .into(holder.imageView)
        }

        holder.menuFishName.text = currentReview.fishKind.toString()
        holder.fishPrice.text = currentReview.cost.toString()
        holder.starScore.text = currentReview.rating.toString()
        holder.reviewContent.text = currentReview.content


//가격 및 멤버이름
        //멤버이름 , 사진
        user?.imageUrl?.let {
            Glide.with(holder.itemView)
                .load(it)
                .into(holder.meberProfil)
        }
        holder.memberName.text = user?.nickname
//
//        //가격
//        holder.minPrice.text = reviewDetail.minPrice.toString()
//        holder.maxPrice.text = reviewDetail.maxPrice.toString()
//        holder.avgPrice.text = reviewDetail.avgPrice.toString()
//
//
//
//        //평균값 계산ㅇ
//        val avgPrice = (reviewDetail.minPrice + reviewDetail.maxPrice) /2
//        holder.avgPrice.text = avgPrice.toString()
//
        //점수에 따른 별 색깔 변화
        val rating = currentReview.rating!!.toFloat()
        holder.ratingBar.rating = rating

//        //가격에 따른 문구 변화
//        val menuprice = reviewDetail.fishPrice
//        val avgprice = reviewDetail.avgPrice
//
//        val Textcomparison = if(menuprice > avgprice){
//            "평균가보다 높아요!"
//        }else{
//            "평균가보다 낮아요!"
//        }
//        holder.Textcomparison.text = Textcomparison
//
//        //가격에 따른 색깔변화
//        if(menuprice > avgprice){
//            holder.Textcomparison.setTextColor(holder.itemView.context.getColor(R.color.red))
//            holder.fishPrice.setTextColor(holder.itemView.context.getColor(R.color.red))
//        }else{
//            holder.Textcomparison.setTextColor(holder.itemView.context.getColor(R.color.blue))
//            holder.fishPrice.setTextColor(holder.itemView.context.getColor(R.color.blue))
//        }

        //드롭다운 버튼

    }


    override fun getItemCount(): Int {
        return review.size
    }
    open fun setReviews(reviews: List<Review>, users: List<Users>) {
        this.review = reviews
        this.users = users
        notifyDataSetChanged()
    }

}