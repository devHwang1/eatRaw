package com.example.eatraw.adapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatraw.ComparingPriceDetailActivity
import com.example.eatraw.R
import com.example.eatraw.data.ComparingPriceItem
import com.google.firebase.firestore.FirebaseFirestore

class FishAdapter(private val context: Context, private val fishList: MutableList<ComparingPriceItem>) :
    RecyclerView.Adapter<FishAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fish_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fish = fishList[position]
        holder.bind(fish)
    }

    override fun getItemCount(): Int {
        return fishList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fishImg: ImageView = itemView.findViewById(R.id.fishImg)
        private val fishName: TextView = itemView.findViewById(R.id.fishName)
        private val fishPriceMax: TextView = itemView.findViewById(R.id.fishPriceMax)
        private val fishPriceMin: TextView = itemView.findViewById(R.id.fishPriceMin)
        private val fishPriceAvg: TextView = itemView.findViewById(R.id.fishPriceAvg)
        private val seasons: TextView = itemView.findViewById(R.id.seasons)

        private val btnDeleteFish: Button = itemView.findViewById(R.id.btnDeleteFish)


        init {
            itemView.setOnClickListener {
                val fish = fishList[adapterPosition]

                // Intent를 사용하여 ComparingPriceDetailActivity로 이동
                val intent = Intent(context, ComparingPriceDetailActivity::class.java)

                // 정보를 번들에 담아서 전달
                val bundle = Bundle()
                bundle.putString("fishName", fish.fishName)
                bundle.putString("minCost", fish.minCost)
                bundle.putString("avgCost", fish.avgCost)
                bundle.putString("maxCost", fish.maxCost)
                bundle.putString("fishImg", fish.fishImg)
                intent.putExtras(bundle)

                context.startActivity(intent)
            }
        }

        init {
            btnDeleteFish.setOnClickListener {
                val fish = fishList[adapterPosition]
                deleteFish(fish) // 삭제 함수 호출
            }
        }

        fun bind(fish: ComparingPriceItem) {
            fishName.text = fish.fishName
            fishPriceMax.text = context.getString(R.string.price_max, fish.maxCost)
            fishPriceMin.text = context.getString(R.string.price_min, fish.minCost)
            fishPriceAvg.text = context.getString(R.string.price_avg, fish.avgCost)
            seasons.text = context.getString(R.string.seasons, fish.season)

            // 이미지 로드 (Glide 사용 예제)
            Glide.with(context)
                .load(fish.fishImg)
                .placeholder(R.drawable.default_nallo)
                .error(R.drawable.default_nallo)
                .into(fishImg)
        }

        private fun deleteFish(fish: ComparingPriceItem) {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("fish")

            // 해당 물고기 항목의 Firestore 문서 ID를 가져옴 (앞서 설명한 방법 사용)
            val documentId = getDocumentIdForFish(fish)

            // Firestore에서 해당 문서 삭제
            if (documentId != null) {
                collectionReference.document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        // 삭제 성공 시, 데이터 목록에서도 제거
                        fishList.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                    }
                    .addOnFailureListener { e ->
                        // 삭제 실패 시 처리
                        Log.e("FirestoreError", "Error deleting document: ", e)
                    }
            }
        }

        // 앞에서 설명한 getDocumentIdForFish 함수 추가
        private fun getDocumentIdForFish(fish: ComparingPriceItem): String? {
            // Firestore 문서 ID를 가져오는 코드
            for (fishItem in fishList) {
                if (fishItem == fish) {
                    return fishItem.fishName
                }
            }
            return null
        }


    }
}


