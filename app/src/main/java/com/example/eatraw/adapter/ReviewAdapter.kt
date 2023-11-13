package com.example.eatraw.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ReviewAdapter(private val reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val reviewContent: TextView = itemView.findViewById(R.id.reviewContent)
        val textView: TextView = itemView.findViewById(R.id.rating)
        val button1: TextView = itemView.findViewById(R.id.button1)
        val storeName: TextView = itemView.findViewById(R.id.storeName)
        val map: LinearLayout = itemView.findViewById(R.id.map)

        // ViewHolder 안에 데이터를 표시할 View 선언
        lateinit var address: String
        lateinit var tel: String
        lateinit var open: String
        lateinit var content: String
        var dbLatitude: Double = 0.0
        var dbLongitude: Double = 0.0

        fun fetchMarketDataAsync(marketName: String, onComplete: (String, String, String, String, Double, Double) -> Unit) {
            val db = FirebaseFirestore.getInstance()
            val docRef = db.collection("map").document(marketName)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val documentSnapshot = docRef.get().await()
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        address = documentSnapshot.getString("address") ?: "주소 정보 없음"
                        tel = documentSnapshot.getString("tel") ?: "전화번호 정보 없음"
                        open = documentSnapshot.getString("open") ?: "영업시간 정보 없음"
                        content = documentSnapshot.getString("mcontent") ?: "시장내용 정보 없음"
                        dbLatitude = documentSnapshot.getDouble("latitude") ?: 0.0
                        dbLongitude = documentSnapshot.getDouble("longitude") ?: 0.0
                    } else {
                        address = "주소 정보 없음"
                        tel = "전화번호 정보 없음"
                        open = "영업시간 정보 없음"
                        content = "시장내용 정보 없음"
                        dbLatitude = 0.0
                        dbLongitude = 0.0
                    }

                    withContext(Dispatchers.Main) {
                        onComplete(address, tel, open, content, dbLatitude, dbLongitude)
                    }
                } catch (exception: Exception) {
                    Log.e("FirestoreError", "Error getting document: ", exception)
                    withContext(Dispatchers.Main) {
                        // Handle the error appropriately.
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.recyclerview_review, parent, false)
        return ReviewViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]

        review.storeImg?.let {
            Glide.with(holder.itemView)
                .load(it)
                .into(holder.imageView)
        }

        holder.reviewContent.text = review.content
        holder.textView.text = review.rating?.toString() ?: "N/A"
        holder.button1.text = review.marketName
        holder.storeName.text = review.storeName

        holder.map.setOnClickListener {
            holder.fetchMarketDataAsync(review.marketName) { address, tel, open, content, dbLatitude, dbLongitude ->
                showMapModal(
                    holder.itemView.context,
                    address,
                    tel,
                    open,
                    content,
                    dbLatitude,
                    dbLongitude
                )
            }
        }

        holder.itemView.setOnClickListener {
            val review = reviews[position]
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("reviewContent", review.content)
            intent.putExtra("marketName", review.marketName)
            intent.putExtra("storeName", review.storeName)
            intent.putExtra("rating", review.rating ?: 0.0)
            intent.putExtra("region", review.region)
            intent.putExtra("fishKind", review.fishKind)
            intent.putExtra("cost", review.cost)
            Log.d("ReviewAdapter", "Cost: ${review.cost}")
            intent.putExtra("userId", review.userId)
            intent.putExtra("image", review.storeImg)
            intent.putExtra("reviewId", review.reviewId)

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
                    } else {
                        holder.itemView.context.startActivity(intent)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("FirestoreError", "Error getting user document: ", exception)
                    holder.itemView.context.startActivity(intent)
                }

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

                    holder.itemView.context.startActivity(intent)
                }
                .addOnFailureListener { exception ->
                    Log.e("FirestoreError", "Error getting fish document: ", exception)
                    holder.itemView.context.startActivity(intent)
                }
        }
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    private fun showMapModal(
        context: Context,
        address: String,
        tel: String,
        open: String,
        content: String,
        dbLatitude: Double,
        dbLongitude: Double
    ) {
        val builder = AlertDialog.Builder(context)

        val dialogLayout = LinearLayout(context)
        dialogLayout.orientation = LinearLayout.VERTICAL
        dialogLayout.setPadding(70, 70, 70, 70)

        dialogLayout.z = 1.0f

        val titleTextView = TextView(context)
        val titleLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        titleLayoutParams.setMargins(0, 0, 0, 50)
        titleTextView.layoutParams = titleLayoutParams
        titleTextView.text = "시장정보"
        titleTextView.textSize = 30f
        titleTextView.setTextColor(Color.BLACK)
        dialogLayout.gravity = Gravity.CENTER
        dialogLayout.addView(titleTextView)

        if (dbLatitude != 0.0 && dbLongitude != 0.0) {
            val mapViewBundle = Bundle()
            val mapView = MapView(context)
            mapView.onCreate(mapViewBundle)

            val mapHeightPx = 900
            val mapLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                mapHeightPx
            )

            mapLayoutParams.setMargins(0, 0, 0, 35)
            mapView.layoutParams = mapLayoutParams

            mapView.getMapAsync { naverMap ->
                val cameraPosition = CameraPosition(LatLng(dbLatitude, dbLongitude), 16.0)
                naverMap.cameraPosition = cameraPosition

                val marker = Marker()
                marker.position = LatLng(dbLatitude, dbLongitude)
                marker.map = naverMap
            }
            dialogLayout.addView(mapView)
        }

        val addressLayout = LinearLayout(context)
        addressLayout.orientation = LinearLayout.HORIZONTAL

        val addressImageView = ImageView(context)
        addressImageView.setImageResource(R.drawable.loc)
        val imageSize = 36
        val params = LinearLayout.LayoutParams(imageSize, imageSize)
        params.setMargins(0, 9, 8, 25)
        addressImageView.layoutParams = params

        val addressTextView = TextView(context)
        addressTextView.text = "$address"
        addressTextView.textSize = 16f

        addressLayout.addView(addressImageView)
        addressLayout.addView(addressTextView)

        dialogLayout.addView(addressLayout)

        val telLayout = LinearLayout(context)
        telLayout.orientation = LinearLayout.HORIZONTAL

        val telImageView = ImageView(context)
        telImageView.setImageResource(R.drawable.phone)
        val telImageViewParams = LinearLayout.LayoutParams(imageSize, imageSize)
        telImageViewParams.setMargins(0, 9, 8, 25)
        telImageView.layoutParams = telImageViewParams

        val telTextView = TextView(context)
        telTextView.text = "$tel"
        telTextView.textSize = 16f

        telLayout.addView(telImageView)
        telLayout.addView(telTextView)

        dialogLayout.addView(telLayout)

        val openLayout = LinearLayout(context)
        openLayout.orientation = LinearLayout.HORIZONTAL

        val openImageView = ImageView(context)
        openImageView.setImageResource(R.drawable.clock)
        val openImageViewParams = LinearLayout.LayoutParams(imageSize, imageSize)
        openImageViewParams.setMargins(0, 9, 8, 25)
        openImageView.layoutParams = openImageViewParams

        val openTextView = TextView(context)
        openTextView.text = "$open"
        openTextView.textSize = 16f

        openLayout.addView(openImageView)
        openLayout.addView(openTextView)

        dialogLayout.addView(openLayout)

        val contentLayout = LinearLayout(context)
        contentLayout.orientation = LinearLayout.HORIZONTAL

        val contentImageView = ImageView(context)
        contentImageView.setImageResource(R.drawable.info)
        val contentImageViewParams = LinearLayout.LayoutParams(imageSize, imageSize)
        contentImageViewParams.setMargins(0, 9, 8, 12)
        contentImageView.layoutParams = contentImageViewParams

        val contentTextView = TextView(context)
        contentTextView.text = "$content"
        contentTextView.textSize = 16f

        contentLayout.addView(contentImageView)
        contentLayout.addView(contentTextView)

        dialogLayout.addView(contentLayout)

        builder.setView(dialogLayout)

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}