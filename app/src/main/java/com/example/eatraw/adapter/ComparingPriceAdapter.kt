package com.example.eatraw.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eatraw.ComparingPriceDetailActivity
import com.example.eatraw.R
import com.example.eatraw.data.ComparingPriceItem

class ComparingPriceAdapter(private val data: List<ComparingPriceItem>) :
    RecyclerView.Adapter<ComparingPriceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comparing_price, parent, false)
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
        private val fishNameTextView: TextView = itemView.findViewById(R.id.fishName)
        private val priceTextView: TextView = itemView.findViewById(R.id.fishPrice)
        private val menuSendImageView: ImageView = itemView.findViewById(R.id.menuSend)
//        private val seeingMoreTextView: TextView = itemView.findViewById(R.id.seeingMore)

        init {
            // menuSend 클릭 리스너 추가
            menuSendImageView.setOnClickListener {
                // 해당 항목에 대한 정보 가져오기
                val fishName = data[adapterPosition].fishName
                val minCost = data[adapterPosition].minCost
                val avgCost = data[adapterPosition].avgCost
                val maxCost = data[adapterPosition].maxCost
                val fishImg = data[adapterPosition].fishImg

                // Intent를 사용하여 ComparingPriceDetailActivity로 이동
                val intent = Intent(itemView.context, ComparingPriceDetailActivity::class.java)

                // 정보를 번들에 담아서 전달
                val bundle = Bundle()
                bundle.putString("fishName", fishName)
                bundle.putLong("minCost", minCost)
                bundle.putLong("avgCost", avgCost)
                bundle.putLong("maxCost", maxCost)
                bundle.putString("fishImg", fishImg)

                intent.putExtras(bundle)
                itemView.context.startActivity(intent)
            }
        }

        fun bind(item: ComparingPriceItem) {
            fishNameTextView.text = item.fishName
            priceTextView.text = item.minCost.toString()
        }

    }

}
