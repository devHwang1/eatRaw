package com.example.eatraw

import android.view.View

interface OnItemClickListener {
    fun onItemClicked(position: Int, view: View)
    fun onItemClick(position: Int)

}