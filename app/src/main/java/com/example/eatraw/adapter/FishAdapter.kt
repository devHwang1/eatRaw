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
import com.example.eatraw.UpdateFishActivity
import com.example.eatraw.data.ComparingPriceItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class FishAdapter(
    private val context: Context,
    private val fishList: MutableList<ComparingPriceItem>
) :
    RecyclerView.Adapter<FishAdapter.ViewHolder>() {

    fun setData(newFishList: List<ComparingPriceItem>) {
        fishList.clear()
        fishList.addAll(newFishList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_fish_list, parent, false)
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
        private val btnUpdateFish: Button = itemView.findViewById(R.id.btnUpdateFish)

        init {
            itemView.setOnClickListener {
                val fish = fishList[adapterPosition]

                // Intent를 사용하여 ComparingPriceDetailActivity로 이동
                val intent = Intent(context, ComparingPriceDetailActivity::class.java)

                // 정보를 번들에 담아서 전달
                val bundle = Bundle()
                bundle.putString("fishName", fish.fishName)
                bundle.putLong("minCost", fish.minCost)
                bundle.putLong("avgCost", fish.avgCost)
                bundle.putLong("maxCost", fish.maxCost)
                bundle.putString("fishImg", fish.fishImg)
                intent.putExtras(bundle)

                context.startActivity(intent)
            }

            btnDeleteFish.setOnClickListener {
                val fish = fishList[adapterPosition]
                deleteFish(fish) // 삭제 함수 호출
            }

            btnUpdateFish.setOnClickListener {
                val fish = fishList[adapterPosition]

                // Intent를 사용하여 수정 페이지로 이동
                val intent = Intent(context, UpdateFishActivity::class.java)

                // 정보를 Intent에 추가
                intent.putExtra("fishName", fish.fishName)
                intent.putExtra("minCost", fish.minCost)
                intent.putExtra("avgCost", fish.avgCost)
                intent.putExtra("maxCost", fish.maxCost)
                intent.putExtra("fishImg", fish.fishImg)
                intent.putExtra("season", fish.season)

                // Firestore에서 해당 물고기의 문서 ID를 가져와서 Intent에 추가
                val documentId = getDocumentIdForFish(fish)
                if (documentId != null) {
                    intent.putExtra("documentId", documentId)  // 문서 ID를 Intent에 추가
                    val documentData = getDocumentDataForFish(documentId)
                    intent.putExtra("fishDocumentData", documentData)
                }

                // 수정 페이지로 이동
                context.startActivity(intent)
            }

        }

        fun bind(fish: ComparingPriceItem) {
            fishName.text = fish.fishName
            fishPriceMax.text = fish.maxCost.toString()
            fishPriceMin.text = fish.minCost.toString()
            fishPriceAvg.text = fish.avgCost.toString()
            seasons.text = fish.season

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

        private fun getDocumentIdForFish(fish: ComparingPriceItem): String? = runBlocking {
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


        private fun getDocumentDataForFish(documentId: String): HashMap<String, Any>? = runBlocking {
            val db = FirebaseFirestore.getInstance()
            val documentReference = db.collection("fish").document(documentId)

            var documentData: HashMap<String, Any>? = null

            try {
                val documentSnapshot = documentReference.get().await()

                if (documentSnapshot.exists()) {
                    documentData = documentSnapshot.data as HashMap<String, Any>
                }
            } catch (exception: Exception) {
                // 오류 처리
                exception.printStackTrace()
            }

            documentData
        }
    }

}
