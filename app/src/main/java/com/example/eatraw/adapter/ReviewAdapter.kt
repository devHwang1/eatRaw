package com.example.eatraw.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatraw.R
import com.example.eatraw.data.Review

class ReviewAdapter(private val reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val reviewContent: TextView = itemView.findViewById(R.id.reviewContent)
        val textView: TextView = itemView.findViewById(R.id.rating)
        val button1: TextView = itemView.findViewById(R.id.button1)
        val storeName : TextView = itemView.findViewById(R.id.storeName)
//        val button2: Button = itemView.findViewById(R.id.button2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]

        // 리뷰 데이터를 뷰에 연결
        // 이미지 URL을 Glide로 로드
        review.storeImg?.let {
            Glide.with(holder.itemView)
                .load(it)
                .into(holder.imageView)
        }
        holder.reviewContent.text = review.content
        holder.textView.text = review.rating?.toString() ?: "N/A"
        holder.button1.text = review.marketName // 시장 이름
        holder.storeName.text = review.storeName
//        holder.button2.text = review.storeName // 가게 이름
    }

    override fun getItemCount(): Int {
        return reviews.size
    }
}