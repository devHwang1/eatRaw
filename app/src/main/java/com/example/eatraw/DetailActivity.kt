package com.example.eatraw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatraw.adapter.ReviewDetailAdapter
import com.example.eatraw.data.DetailReview
import com.example.eatraw.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : ReviewDetailAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.detailRecylerView

        recyclerView.layoutManager = LinearLayoutManager(this)


        val detailData = createDetailData()
        adapter = ReviewDetailAdapter(detailData)

        recyclerView.adapter = adapter




    }

    private fun createDetailData(): List<DetailReview> {
        val detailReviews = mutableListOf<DetailReview>()
        detailReviews.add(DetailReview(R.drawable.ic_launcher_background, "고등어", 50000,"회사랑",1.5,50000,20000,2000,"안녕"))
        detailReviews.add(DetailReview(R.drawable.test_image, "광어", 90000,"어느회사원",4.5,90000,20000, 3231,"맛있네요 맛있어요..사장님도 친절하십니다..."))
        detailReviews.add(DetailReview(R.drawable.test_image, "참돔", 90000,"어느회사원",2.0,50000,20000, 2000,"우선 내부가 깨끗해서 위생걱정은 없습니다. 회는 정말 싱싱하고 맛있었습니다.가격이 좀 높지만 그만큼 회가 많아 가성비가 안좋다는 느낌은 안들었습니다."))
        detailReviews.add(DetailReview(R.drawable.test_image, "고등어", 300,"어느회사원",3.5,40000,10000, 2000,"우선 내부가 깨끗해서 위생걱정은 없습니다. 회는 정말 싱싱하고 맛있었습니다.가격이 좀 높지만 그만큼 회가 많아 가성비가 안좋다는 느낌은 안들었습니다."))

        return detailReviews
    }
}