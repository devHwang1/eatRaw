package com.example.eatraw

import android.view.View
import com.example.eatraw.adapter.ReviewAdapter

interface OnItemClickListener {
    fun onItemClicked(position: Int, view: View)
    fun onItemClick(position: Int)

}