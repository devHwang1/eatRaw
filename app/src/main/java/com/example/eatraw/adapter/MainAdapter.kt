package com.example.eatraw.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eatraw.databinding.ItemMainBinding
import com.example.eatraw.model.Affirmation


// Adapter는 데이터를 RecyclerView에 추가하는 객체
class MainAdapter(val datas: List<Affirmation>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // ViewHolder는 RecyclerView의 하나의 항목을 담기 위한 틀
    class MyViewHolder(val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root)

    // RecyclerView의 항목의 갯수
    // ViewHolder의 size
    override fun getItemCount(): Int = datas.size

    // ViewHolder 하나를 생성하기 위한 메서드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    // ViewHolder에 데이터를 주입하는 메서드
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(">>", "onBindViewHolder : $position")
        val binding = (holder as MyViewHolder).binding
        binding.itemData.text = datas.get(position).strTitle
        binding.itemRoot.setOnClickListener {
            Log.d(">>", "item root click : $position")
        }
    }
}