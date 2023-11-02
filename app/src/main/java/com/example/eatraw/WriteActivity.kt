import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import com.example.eatraw.R
import com.example.eatraw.databinding.ActivityWriteBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference

class WriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWriteBinding
    private lateinit var db: FirebaseFirestore

    private lateinit var editStoreName: EditText
    private lateinit var editFishName: EditText
    private lateinit var editFishPrice: EditText
    private lateinit var starScore: RatingBar
    private lateinit var editText: EditText
    private lateinit var btnReview: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase 초기화
        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()

        // XML 레이아웃에서 뷰들을 참조
        editStoreName = findViewById(R.id.editStoreName)
        editFishName = findViewById(R.id.editFishName)
        editFishPrice = findViewById(R.id.editFishPrice)
        starScore = findViewById(R.id.starScore)
        editText = findViewById(R.id.editText)
        btnReview = findViewById(R.id.btnReview)

        // 리뷰 작성 버튼 클릭 이벤트 설정
        btnReview.setOnClickListener {
            addReviewToFirestore()
        }
    }

    // Firestore에 리뷰 데이터 추가
    private fun addReviewToFirestore() {
        val content = editText.text.toString()
        val fishName = editFishName.text.toString()
        val storeImg = "" // 이미지 URL 또는 이미지 참조를 여기에 추가
        val fishPrice = editFishPrice.text.toString().toDoubleOrNull() ?: 0.0
        val rating = starScore.rating.toDouble()
        val storeName = editStoreName.text.toString()

        // 리뷰 데이터를 Firestore에 추가
        val reviewData = hashMapOf(
            "content" to content,
            "fishKind" to fishName,
            "storeImg" to storeImg,
            "cost" to fishPrice,
            "rating" to rating,
            "storeName" to storeName
        )

        db.collection("review")
            .add(reviewData)
            .addOnSuccessListener { documentReference: DocumentReference ->
                // 성공적으로 추가되었을 때의 처리
                val reviewId = documentReference.id
                // 리뷰가 Firestore에 추가되었습니다.
                // 필요한 경우 리뷰 ID (reviewId)를 사용할 수 있습니다.
            }
            .addOnFailureListener { e ->
                // 추가 중에 오류가 발생한 경우 처리
            }
    }
}
