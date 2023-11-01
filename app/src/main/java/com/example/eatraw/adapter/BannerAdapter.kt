package com.example.eatraw.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eatraw.R
import com.example.eatraw.data.BannerItem

class BannerAdapter(private val data: List<BannerItem>) :
    RecyclerView.Adapter<BannerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_banner, parent, false)
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
        private val imageView: ImageView = itemView.findViewById(R.id.bannerImage)
//        private val titleTextView: TextView = itemView.findViewById(R.id.bannerTitle)
//        private val descriptionTextView: TextView = itemView.findViewById(R.id.bannerDescription)

        fun bind(item: BannerItem) {
            imageView.setImageResource(item.imageResId)
//            titleTextView.text = item.title // BannerItem에 해당하는 필드를 지정해야 합니다.
//            descriptionTextView.text = item.description // BannerItem에 해당하는 필드를 지정해야 합니다.
        }
    }
}

