package com.example.eatraw.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eatraw.R
import com.example.eatraw.data.DetailReview

class ReviewDetailAdapter(private val detailReviews: List<DetailReview>) :
    RecyclerView.Adapter<ReviewDetailAdapter.ReviewDetailAdapterViewHolder>() {
    class ReviewDetailAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView : ImageView = itemView.findViewById(R.id.Reviewimg)
        val menuFishName : TextView = itemView.findViewById(R.id.MenuFishName)
        val fishPrice : TextView = itemView.findViewById(R.id.StorePriceInt)
        val memberName : TextView = itemView.findViewById(R.id.mName)
        val starScore: TextView = itemView.findViewById(R.id.mStarsocore)
        val contentView : TextView = itemView.findViewById(R.id.contentView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): ReviewDetailAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_detail_box, parent, false)
        return ReviewDetailAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewDetailAdapterViewHolder, position: Int) {
        val reviewDetail = detailReviews[position]

        holder.imageView.setImageResource(reviewDetail.MenuResource)
        holder.menuFishName.text = reviewDetail.fishiName
        holder.fishPrice.text = reviewDetail.fishPrice.toString()
        holder.memberName.text = reviewDetail.memberName
        holder.starScore.text = reviewDetail.score.toString()
        holder.contentView.text = reviewDetail.textcontent
    }


    override fun getItemCount(): Int {
        return detailReviews.size
    }


}