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
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class FishAdapter(private val context: Context, private val fishList: MutableList<ComparingPriceItem>) :
    RecyclerView.Adapter<FishAdapter.ViewHolder>() {

    fun setData(newFishList: List<ComparingPriceItem>) {
        fishList.clear()
        fishList.addAll(newFishList)
        notifyDataSetChanged()
    }

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
            fishPriceMax.text = fish.maxCost
            fishPriceMin.text = fish.minCost
            fishPriceAvg.text = fish.avgCost
            seasons.text = fish.season
//            fishPriceMax.text = context.getString(R.string.price_max, fish.maxCost)
//            fishPriceMin.text = context.getString(R.string.price_min, fish.minCost)
//            fishPriceAvg.text = context.getString(R.string.price_avg, fish.avgCost)
//            seasons.text = context.getString(R.string.seasons, fish.season)
//            Log.e("넘무넘무해>>", "${fish}")

            // 이미지 로드 (Glide 사용 예제)
            Glide.with(context)
                .load(fish.fishImg)
                .placeholder(R.drawable.default_nallo)
                .error(R.drawable.default_nallo)
                .into(fishImg)
        }

        private fun deleteFish(fish: ComparingPriceItem) {
            val db = FirebaseFirestore.getInstance()
            val collectionReference = db.collection("fish")

            val documentId: String? = getDocumentIdForFish(fish)

            if (documentId != null) {
                collectionReference.document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        // 삭제 성공 시, 데이터 목록에서도 제거
                        val deletedIndex = fishList.indexOfFirst { it.fishName == fish.fishName }
                        if (deletedIndex != -1) {
                            fishList.removeAt(deletedIndex)
                            notifyItemRemoved(deletedIndex)
                        }
                    }
                    .addOnFailureListener { e ->
                        // 삭제 실패 시 처리
                        Log.e("FirestoreError", "Error deleting document: ", e)
                    }
            }
        }

        fun getDocumentIdForFish(fish: ComparingPriceItem): String? = runBlocking{
            // 일반적으로 suspend 함수를 사용...다음부터는 그렇게 만들 것!!
            val db = FirebaseFirestore.getInstance()
            val collectionReference = db.collection("fish")

            var documentId: String? = null

            try {
                val querySnapshot = collectionReference
                    .whereEqualTo("f_name", fish.fishName)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    documentId = document.id
                    break
                }
            } catch (exception: Exception) {
                // 오류 처리
                exception.printStackTrace()
            }

            documentId
        }
    }
}
