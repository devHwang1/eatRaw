package com.example.eatraw.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatraw.DetailActivity
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
            imageView.setOnClickListener {
                // 여기에서 고유 식별자로 해당 리뷰로 이동하는 코드를 추가합니다.
                val context = imageView.context
                val reviewId = item.imageResId.toString() // imageResId를 고유 식별자로 사용 (고유 식별자 형식에 따라 변환해야 할 수 있음)
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("reviewId", reviewId)
                context.startActivity(intent)
            }

            // 이미지를 로드하고 텍스트 설정
            imageView.setImageResource(item.imageResId)
            textView.text = item.rating.toString()
        }


//        fun bind(item: BestReviewItem) {
//            // 이미지 클릭 이벤트 처리
//            imageView.setOnClickListener {
//                // 여기에서 해당 리뷰로 이동하는 코드를 추가합니다
//                val context = itemView.context
//                val reviewId = item.reviewId  // 리뷰의 고유 ID 또는 다른 식별자를 가져옵니다
//                val intent = Intent(context, DetailActivity::class.java)
//                intent.putExtra("reviewId", reviewId)
//                context.startActivity(intent)
//            }
//
//            // 이미지를 로드하고 텍스트 설정
//            imageView.setImageResource(item.imageResId)
//            textView.text = item.rating.toString()
//        }
    }
}

private fun ImageView.setImageResource(imageUrl: String?) {
    if (imageUrl != null) {
        Glide.with(this.context)
            .load(imageUrl)
            .into(this)
    } else {
        this.setImageResource(R.drawable.fisherman)
    }
}

