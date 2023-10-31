package com.example.eatraw.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eatraw.R
import com.example.eatraw.data.Review


class ReviewAdapter(private val reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val reviewContent: TextView = itemView.findViewById(R.id.reviewContent)
        val reviewStar: ImageView = itemView.findViewById(R.id.reviewStar)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val button1: Button = itemView.findViewById(R.id.button1)
        val button2: Button = itemView.findViewById(R.id.button2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]

        // 리뷰 데이터를 뷰에 연결
        holder.imageView.setImageResource(review.imageResource)
        holder.reviewContent.text = review.content
        holder.reviewStar.setImageResource(review.starImageResource)
        holder.textView.text = review.rating
        holder.button1.text = review.button1Text
        holder.button2.text = review.button2Text
    }

    override fun getItemCount(): Int {
        return reviews.size
    }
}
