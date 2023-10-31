package com.example.eatraw.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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

        fun bind(item: ComparingPriceItem) {
            fishNameTextView.text = item.fishName
            priceTextView.text = item.price
        }
    }
}

