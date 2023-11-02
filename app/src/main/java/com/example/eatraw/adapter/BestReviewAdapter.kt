package com.example.eatraw.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eatraw.R
import com.example.eatraw.data.BestReviewItem

class BestReviewAdapter(private val data: List<BestReviewItem>) :
    RecyclerView.Adapter<BestReviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_best_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.reviewImage)
        private val textView: TextView = itemView.findViewById(R.id.reviewRating)

        fun bind(item: BestReviewItem) {
            imageView.setImageResource(item.imageResId)
            textView.text = item.rating.toString()
        }
    }
}