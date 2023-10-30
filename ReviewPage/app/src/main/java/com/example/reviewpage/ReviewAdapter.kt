package com.example.reviewpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    // ViewHolder 클래스를 만들어봅니다.
    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ViewHolder 내에서 각 뷰에 접근하는 변수들을 선언할 수 있습니다.
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val reviewContent: TextView = itemView.findViewById(R.id.reviewContent)
        val reviewStar: ImageView = itemView.findViewById(R.id.reviewStar)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val button1: Button = itemView.findViewById(R.id.button1)
        val button2: Button = itemView.findViewById(R.id.button2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        // 뷰홀더를 생성하고 리사이클러뷰 아이템의 레이아웃 파일을 연결합니다.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        // 뷰홀더에 데이터를 연결합니다. position은 현재 아이템의 위치입니다.
        // 예를 들어, holder.imageView.setImageDrawable()와 같이 데이터를 설정할 수 있습니다.
        // 이 데이터는 RecyclerView에 나타날 각 아이템의 정보입니다.
    }

    override fun getItemCount(): Int {
        // 리사이클러뷰에 표시할 아이템의 개수를 반환합니다.
        return 0 // 아직 아이템 개수를 설정하지 않았으므로 0으로 설정했습니다.
    }
}