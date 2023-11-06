package com.example.eatraw

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.eatraw.databinding.ActivityMypageBinding
import com.example.eatraw.mypagefrg.AlarmFragment
import com.example.eatraw.mypagefrg.CenterFragment
import com.example.eatraw.mypagefrg.DelFragment
import com.example.eatraw.mypagefrg.GoogleDelFragment
import com.example.eatraw.mypagefrg.ModifyFragment
import com.example.eatraw.mypagefrg.MyreviewFragment
import com.example.eatraw.mypagefrg.PasswordFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MypageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMypageBinding
    private lateinit var TextView: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var aouthLogin: Boolean? = null
    private lateinit var btn_del:TextView


    //    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        navController = nav_host_fragment.findNavController()

        val reviewLayout = findViewById<TextView>(R.id.myreview)
        val alarmLayout = findViewById<TextView>(R.id.alarm)
        val centerLayout = findViewById<TextView>(R.id.center)
        val email = findViewById<TextView>(R.id.email)
        val user = FirebaseAuth.getInstance().currentUser
        btn_del = binding.btnDel
        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        email.text = user?.email

        val userId = mAuth.currentUser?.uid
        userId?.let {
            getUserInfo(it)
        }


        //개별 프래그먼트로 이동
        //정보 수정은 구글 로그인 이슈로 다른 함수에 있음

        reviewLayout.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MyreviewFragment()).addToBackStack(null).commit()
        }
        alarmLayout.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AlarmFragment()).addToBackStack(null).commit()
        }
        centerLayout.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CenterFragment()).addToBackStack(null).commit()
        }
        TextView = findViewById(R.id.logout)
        TextView.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        btn_del.setOnClickListener {
            if (aouthLogin == true) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, GoogleDelFragment()).addToBackStack(null).commit()
            }else{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, DelFragment()).addToBackStack(null).commit()
            }


        }


        var bnv_main = findViewById(R.id.bnv_main) as BottomNavigationView

        // OnNavigationItemSelectedListener를 통해 탭 아이템 선택 시 이벤트를 처리
        // navi_menu.xml 에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.
        bnv_main.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.first -> {
                        // 다른 액티비티로 이동
                        val intent = Intent(this@MypageActivity, MainActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.second -> {
                        // 다른 액티비티로 이동
                        val intent = Intent(this@MypageActivity, ReviewActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.third -> {
                        // 다른 액티비티로 이동
                        val intent =
                            Intent(this@MypageActivity, ComparingPriceListActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.four -> {
                        // 다른 액티비티로 이동
                        val intent = Intent(this@MypageActivity, MypageActivity::class.java)
                        startActivity(intent)
                    }
                }
                true
            }


        }

    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    fun getUserInfo(userId: String) {
        val docRef = db.collection("users").document(userId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val nickname = document.getString("nickname")
                    val thumbnailUrl = document.getString("imageUrl")
                    aouthLogin = document.getBoolean("aouthLogin") ?: false

                    if (nickname != null) {
                        val nickHelloTextView = findViewById<TextView>(R.id.nickhello)
                        nickHelloTextView.text = "$nickname 님, 반갑습니다!"
                    } else {
                        println("No such nickname")
                    }

                    // 클릭 리스너 설정
                    val modifyLayout = findViewById<TextView>(R.id.modify)
                    if (aouthLogin!!) {
                        modifyLayout.setOnClickListener {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, ModifyFragment()).addToBackStack(null).commit()
                        }
                    } else {
                        modifyLayout.setOnClickListener {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, PasswordFragment()).addToBackStack(null).commit()
                        }
                    }

                    if (thumbnailUrl != null) {
                        val thumbnailImageView = findViewById<ImageView>(R.id.thumbnail)
                        Glide.with(this).load(thumbnailUrl).placeholder(R.drawable.group_40).circleCrop().into(thumbnailImageView)
                    } else {
                        println("No such thumbnailUrl")
                    }

                } else {
                    println("No such document")
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }
}