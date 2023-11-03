package com.example.eatraw.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eatraw.R
import com.example.eatraw.data.DetailReview


class ReviewDetailAdapter(private val detailReviews: List<DetailReview>) :
    RecyclerView.Adapter<ReviewDetailAdapter.ReviewDetailAdapterViewHolder>() {
    class ReviewDetailAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView : ImageView = itemView.findViewById(R.id.Reviewimg)
        val menuFishName : TextView = itemView.findViewById(R.id.MenuFishName)
        val fishPrice : TextView = itemView.findViewById(R.id.StorePriceInt)
        val memberName : TextView = itemView.findViewById(R.id.mName)
        val starScore: TextView = itemView.findViewById(R.id.mStarsocore)
        val ratingBar : RatingBar = itemView.findViewById(R.id.DratingBar)
        val contentView : TextView = itemView.findViewById(R.id.contentView)
        val Textcomparison : TextView = itemView.findViewById(R.id.Textcomparison)

        //가격
        val minPrice : TextView = itemView.findViewById(R.id.IntMin)
        val maxPrice : TextView = itemView.findViewById(R.id.IntMax)
        val avgPrice : TextView = itemView.findViewById(R.id.IntAvg)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): ReviewDetailAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_detail_box, parent, false)
        return ReviewDetailAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewDetailAdapterViewHolder, position: Int) {
        val reviewDetail = detailReviews[position]

        holder.imageView.setImageResource(reviewDetail.MenuResource)
        holder.menuFishName.text = reviewDetail.fishiName
        holder.fishPrice.text = reviewDetail.fishPrice.toString()
        holder.memberName.text = reviewDetail.memberName
        holder.starScore.text = reviewDetail.score.toString()
        holder.contentView.text = reviewDetail.textcontent.toString()

        holder.minPrice.text = reviewDetail.minPrice.toString()
        holder.maxPrice.text = reviewDetail.maxPrice.toString()
        holder.avgPrice.text = reviewDetail.avgPrice.toString()



        //평균값 계산ㅇ
        val avgPrice = (reviewDetail.minPrice + reviewDetail.maxPrice) /2
        holder.avgPrice.text = avgPrice.toString()

        //점수에 따른 별 색깔 변화
        val rating = reviewDetail.score!!.toFloat()
        holder.ratingBar.rating = rating

        //가격에 따른 문구 변화
        val menuprice = reviewDetail.fishPrice
        val avgprice = reviewDetail.avgPrice

        val Textcomparison = if(menuprice > avgprice){
            "평균가보다 높아요!"
        }else{
            "평균가보다 낮아요!"
        }
        holder.Textcomparison.text = Textcomparison

        //가격에 따른 색깔변화
        if(menuprice > avgprice){
            holder.Textcomparison.setTextColor(holder.itemView.context.getColor(R.color.red))
            holder.fishPrice.setTextColor(holder.itemView.context.getColor(R.color.red))
        }else{
            holder.Textcomparison.setTextColor(holder.itemView.context.getColor(R.color.blue))
            holder.fishPrice.setTextColor(holder.itemView.context.getColor(R.color.blue))
        }


    }


    override fun getItemCount(): Int {
        return detailReviews.size
    }


}