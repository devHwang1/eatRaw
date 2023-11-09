package com.example.eatraw.adapter

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eatraw.banner.Banner1Fragment
import com.example.eatraw.banner.Banner2Fragment
import com.example.eatraw.banner.Banner3Fragment

class BannerFragmentAdapter(fragmentActivity: FragmentActivity, private val images: List<Int>) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = images.size

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> {
                val fragment = Banner1Fragment()
                fragment.arguments = bundleOf("imageRes" to images[position])
                fragment
            }
            1 -> {
                val fragment = Banner2Fragment()
                fragment.arguments = bundleOf("imageRes" to images[position])
                fragment
            }
            2 -> {
                val fragment = Banner3Fragment()
                fragment.arguments = bundleOf("imageRes" to images[position])
                fragment
            }
            else -> {
                // 기본값으로 첫 번째 Fragment를 반환
                val fragment = Banner1Fragment()
                fragment.arguments = bundleOf("imageRes" to images[position])
                fragment
            }
        }
    }
}
