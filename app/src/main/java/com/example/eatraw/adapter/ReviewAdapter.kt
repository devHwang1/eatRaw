import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eatraw.DetailActivity
import com.example.eatraw.R
import com.example.eatraw.data.Review
import com.google.firebase.firestore.FirebaseFirestore

class ReviewAdapter(private val reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val reviewContent: TextView = itemView.findViewById(R.id.reviewContent)
        val textView: TextView = itemView.findViewById(R.id.rating)
        val button1: TextView = itemView.findViewById(R.id.button1)
        val storeName : TextView = itemView.findViewById(R.id.storeName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_review, parent, false)
        return ReviewViewHolder(view)
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

        holder.itemView.setOnClickListener {
            val review = reviews[position]
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("reviewContent", review.content) // 콘텐츠(댓글내용)
            intent.putExtra("marketName", review.marketName)  // 시장이름
            intent.putExtra("storeName", review.storeName)  // 가게이름
            intent.putExtra("rating", review.rating ?: 0.0) // 별점
            intent.putExtra("region", review.region)    // 지역
            intent.putExtra("userId", review.userId)    // 회원이름
            intent.putExtra("image", review.storeImg)   // 사진
            intent.putExtra("fishkindcost",review.cost) // 물고기이름
            intent.putExtra("menuCost",review.fishKind) //물고기 가격



            //파이어 베이스 사용
            val db = FirebaseFirestore.getInstance()



            // 사용자 정보(닉네임) 추가
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
                        // 사용자 정보를 찾지 못한 경우에 대한 처리
                        holder.itemView.context.startActivity(intent)
                    }
                }
                .addOnFailureListener { exception ->
                    // 에러 처리
                    Log.e("FirestoreError", "Error getting user document: ", exception)
                }

            //좋아요 기능
            db.collection("revew")
        }

    }

    override fun getItemCount(): Int {
        return reviews.size
    }
}
