    import android.content.ContentValues.TAG
    import android.content.Intent
    import android.net.Uri
    import android.os.Bundle
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.AdapterView
    import android.widget.ArrayAdapter
    import android.widget.Button
    import android.widget.ImageView
    import android.widget.Spinner
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import androidx.fragment.app.Fragment
    import com.bumptech.glide.Glide
    import com.example.eatraw.MainActivity
    import com.example.eatraw.databinding.FragmentNickBinding
    import com.google.android.material.textfield.TextInputEditText
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.firestore.FirebaseFirestore
    import com.google.firebase.storage.FirebaseStorage
    import java.util.UUID

    class NickFragment : Fragment() {

        private lateinit var binding: FragmentNickBinding
        private lateinit var nickname: TextInputEditText
        private lateinit var thumbnail: ImageView
        private lateinit var btnRegister: Button
        private lateinit var nickcheck:Button
        private lateinit var spinner1: Spinner
        private lateinit var spinner2: Spinner
        private val db = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        private var selectedImageUri: Uri? = null
        private var nickBoolean = false

        companion object {
            const val PICK_IMAGE_REQUEST = 1
        }
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            arguments?.let {
            }
        }
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = FragmentNickBinding.inflate(inflater, container, false)
            nickname = binding.nickname
            thumbnail = binding.thumbnail
            btnRegister = binding.btnRegister
            nickcheck = binding.nickcheck
            spinner1 = binding.spinner1
            spinner2 = binding.spinner2

            val email = arguments?.getString("email")
            Log.d("NickFragment", "Email: $email")
            thumbnail.setOnClickListener{
                openGallery()
            }
            binding.root.setOnClickListener { }
            nickcheck.setOnClickListener {
                val nick = nickname.text.toString()
                checkNickname(nick)
                if (nick.isEmpty() ) {
                    Toast.makeText(context, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }else if(!nickBoolean){
                    return@setOnClickListener
                }
            }
            btnRegister.setOnClickListener {

                if(!nickBoolean){
                    Toast.makeText(context, "닉네임을 확인해주세요", Toast.LENGTH_SHORT).show()
                }else if (spinner2.selectedItem.toString().equals("선택하세요")) {
                    Toast.makeText(context, "마켓을 선택해주세요", Toast.LENGTH_SHORT).show()
                }else{
                    uploadImage()
                }
            }

            val region = ArrayList<String>()
            region.add("지역")
            val spinner1Adapter =
                context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, region) }
            if (spinner1Adapter != null) {
                spinner1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            db.collection("region")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val regionName = document.id
                        region.add(regionName)
                    }
                    if (spinner1Adapter != null) {
                        spinner1Adapter.notifyDataSetChanged()
                    }
                    Log.w("REviewActivity성공@@@@@@@^#^#^#^", "에러내용: 성공이요")
                }
                .addOnFailureListener { exception ->
                    Log.w("REviewActivity에러@@@@@@", "에러내용: $exception")
                }

            spinner1.adapter = spinner1Adapter

            spinner1.setSelection(0, false)

            var markets = ArrayList<String>()
            val spinner2Adapter =
                context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, markets) }
            if (spinner2Adapter != null) {
                spinner2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            spinner2.adapter = spinner2Adapter

//스피너 이벤트 핸들링
            spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedRegion = region[position]
                    updateSpinner2(selectedRegion)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                private fun updateSpinner2(selectedRegion: String) {
                    db.collection("region").document(selectedRegion)
                        .get()
                        .addOnSuccessListener { document ->
                            val marketList = document.get("markets") as List<String>
                            markets.clear()
                            markets.add("선택하세요")
                            markets.addAll(marketList)
                            (spinner2.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                        }
                        .addOnFailureListener { exception ->
                            Log.w("@@@@@@@@@@@@@@@@@씰패", "실패: $exception")
                        }
                }
            }

           return binding.root
        }

        private fun openGallery() {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
                if (data != null) {
                    // 선택한 이미지의 URI 가져오기
                    selectedImageUri = data.data

                    // Glide를 사용하여 이미지 표시
                    Glide.with(this).load(selectedImageUri).circleCrop().into(thumbnail)
                }
            }
        }

        private fun uploadImage() {
            if (selectedImageUri != null) {
                val imageRef = storageRef.child("images/${UUID.randomUUID()}")

                // 이미지 업로드

                val uploadTask = imageRef.putFile(selectedImageUri!!)
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    imageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result
                        saveImageUrlToDatabase(downloadUrl.toString())
                    } else {
                        Toast.makeText(context, "img upload failed.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if(selectedImageUri == null){
                saveImageUrlToDatabase("")
            }
        }

        private fun saveImageUrlToDatabase(imageUrl: String) {
            val db = FirebaseFirestore.getInstance()
            // 프래그먼트에서는 intent를 직접 받을 수 없으므로 아래와 같이 변경
            val mAuth = FirebaseAuth.getInstance()
            val currentUser = mAuth.currentUser
            val userRef = db.collection("users").document(currentUser?.uid!!)
            // Firestore에 사용자 정보 저장
            val user  = hashMapOf<String, Any>(
                "nickname" to nickname.text.toString(),
                "imageUrl" to imageUrl,
                "likeMarket" to spinner2.selectedItem.toString()
            )

            userRef.update(user)
                .addOnSuccessListener { e ->
                    Toast.makeText(
                        context,
                        "환영합니다!",
                        Toast.LENGTH_SHORT
                    ).show()

                    // 데이터베이스 업데이트가 성공한 후 MainActivity로 이동
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        context,
                        "Update failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }
        private fun checkNickname(nickname: String) {
            val usersCollection = FirebaseFirestore.getInstance().collection("users")

            usersCollection.whereEqualTo("nickname", nickname).get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        // 닉네임이 중복되지 않음
                        Toast.makeText(context, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show()
                        nickBoolean = true
                    } else {
                        // 닉네임이 중복됨
                        Toast.makeText(context, "이미 사용 중인 닉네임입니다.", Toast.LENGTH_SHORT).show()
                        nickBoolean = false
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        }
        override fun onStop() {
            super.onStop()

            // 닉네임 입력이 완료되지 않은 상태라면, 가입을 취소합니다.
            if (!nickBoolean) {
                cancelRegistration()
            }
        }

        private fun cancelRegistration() {
            val user = FirebaseAuth.getInstance().currentUser

            user?.delete()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User account deleted.")
                    }
                }
        }
    }