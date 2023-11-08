package com.example.eatraw.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatraw.DetailActivity
import com.example.eatraw.R
import com.example.eatraw.data.Review
import com.google.firebase.firestore.FirebaseFirestore
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.naver.maps.map.overlay.Marker

class ReviewAdapter(private val reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(itemView: View,private val context: Context) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val reviewContent: TextView = itemView.findViewById(R.id.reviewContent)
        val textView: TextView = itemView.findViewById(R.id.rating)
        val button1: TextView = itemView.findViewById(R.id.button1)
        val storeName: TextView = itemView.findViewById(R.id.storeName)
        val map: LinearLayout = itemView.findViewById(R.id.map)
        val mapLayout: View = itemView.findViewById(R.id.map)
        val mapModalLayout = LayoutInflater.from(context).inflate(R.layout.map_modal, null)
        val addressTextView: TextView = mapModalLayout.findViewById(R.id.addressTextView)
        val telTextView : TextView= mapModalLayout.findViewById(R.id.telTextView)
        val openTextView : TextView= mapModalLayout.findViewById(R.id.openTextView)
        val contentTextView : TextView= mapModalLayout.findViewById(R.id.contentTextView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val context = parent.context // 뷰의 컨텍스트를 얻음
        val view = LayoutInflater.from(context).inflate(R.layout.recyclerview_review, parent, false)
        return ReviewViewHolder(view, context) // context를 생성자에 전달
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]

        // 리뷰 데이터를 뷰에 연결
        // 이미지 URL을 Glide로 로드
        review.storeImg?.let {
            Glide.with(holder.itemView)
                .load(it)
                .into(holder.imageView)
        }

        holder.reviewContent.text = review.content
        holder.textView.text = review.rating?.toString() ?: "N/A"
        holder.button1.text = review.marketName // 시장 이름
        holder.storeName.text = review.storeName


        // map 뷰 클릭 리스너 설정
        holder.map.setOnClickListener {
            showMapModal(
                holder.itemView.context,
                holder.addressTextView,
                holder.telTextView,
                holder.openTextView,
                holder.contentTextView,

            )
        }

        holder.itemView.setOnClickListener {
            val review = reviews[position]
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("reviewContent", review.content)
            intent.putExtra("marketName", review.marketName)
            intent.putExtra("storeName", review.storeName)
            intent.putExtra("rating", review.rating ?: 0.0)
            intent.putExtra("region", review.region)
            intent.putExtra("fishKind", review.fishKind.toString())
            intent.putExtra("cost", review.cost)
            intent.putExtra("userId", review.userId)
            intent.putExtra("image", review.storeImg)

            // 사용자 정보(닉네임) 추가
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(review.userId.toString())
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val userNicName = documentSnapshot.getString("nickname")
                        val userImage = documentSnapshot.getString("imageUrl")

                        intent.putExtra("userNickname", userNicName)
                        intent.putExtra("userImage", userImage)
                        holder.itemView.context.startActivity(intent)
                    } else {
                        holder.itemView.context.startActivity(intent)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("FirestoreError", "Error getting user document: ", exception)
                }

            // Fish 종류 정보 추가
            db.collection("fish")
                .document(review.fishKind.toString())
                .get()
                .addOnSuccessListener { fishDocument ->
                    if (fishDocument.exists()) {
                        val minCost = fishDocument.getString("f_min")
                        val avgCost = fishDocument.getLong("f_avg")
                        val maxCost = fishDocument.getLong("f_max")

                        intent.putExtra("fishMin", minCost)
                        intent.putExtra("fishAvg", avgCost)
                        intent.putExtra("fishMax", maxCost)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("FirestoreError", "Error getting fish document: ", exception)
                }
        }
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    private fun showMapModal(
        context: Context,
        addressTextView: TextView,
        telTextView: TextView,
        openTextView: TextView,
        contentTextView: TextView
    ) {
        Log.d("Debug", "showMapModal started")
        Log.w("addressTextView", "$addressTextView")

        // Firestore에서 데이터 가져오기
        val db = FirebaseFirestore.getInstance()
        val marketName = "자갈치시장" // 원하는 시장 이름으로 변경

        db.collection("map")
            .document(marketName)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentSnapshot = task.result
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        val tel = documentSnapshot.getString("tel")
                        val open = documentSnapshot.getString("open")
                        val content = documentSnapshot.getString("mcontent")
                        val dbLatitude = documentSnapshot.getDouble("latitude")
                        val dbLongitude = documentSnapshot.getDouble("longitude")

                        // Firestore에서 가져온 데이터를 새로운 변수에 할당
                        val marketLatitude: Double? = dbLatitude
                        val marketLongitude: Double? = dbLongitude

                        // 나머지 내용은 동일하게 유지
                        telTextView.text = "전화번호: $tel"
                        openTextView.text = "영업 시간: $open"
                        contentTextView.text = "시장 얘기: $content"
                        Log.w("sdfsdfsdfsdfsdf","$marketLongitude")
                        Log.w("sdfsdfsdfsdfsdf","$marketLatitude")
                        Log.w("sdfsdfsdfsdfsdf","$tel")
                        Log.w("sdfsdfsdfsdfsdf","$open")
                        Log.w("sdfsdfsdfsdfsdf","$content")
                        // 새로운 변수를 사용하여 지도에 마크 표시
                        if (marketLatitude != null && marketLongitude != null) {
                            val mapViewBundle = Bundle()
                            val mapView = MapView(context)
                            mapView.onCreate(mapViewBundle)

                            mapView.getMapAsync { naverMap ->
                                val cameraPosition = CameraPosition(LatLng(marketLatitude, marketLongitude), 16.0)
                                naverMap.cameraPosition = cameraPosition

                                val marker = Marker()
                                marker.position = LatLng(marketLatitude, marketLongitude)
                                marker.map = naverMap
                            }
                        }
                    } else {
                        // Document가 없을 때 처리
                    }
                } else {
                    // Firestore 작업 실패 시 처리
                    val exception = task.exception
                    if (exception != null) {
                        Log.e("FirestoreError", "Error getting market document: ", exception)
                    }
                }
            }
        val mapViewBundle = Bundle()
        val mapView = MapView(context)
        mapView.onCreate(mapViewBundle)
        val builder = AlertDialog.Builder(context)
        val dialogLayout = LayoutInflater.from(context).inflate(R.layout.map_modal, null)
        val mapLayout = dialogLayout.findViewById<LinearLayout>(R.id.map)
        // 지도를 모달 창에 추가
        builder.setView(dialogLayout)

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}
