package com.example.eatraw

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.eatraw.databinding.ActivityMypageBinding
import com.example.eatraw.mypagefrg.AlarmFragment
import com.example.eatraw.mypagefrg.CenterFragment
import com.example.eatraw.mypagefrg.ModifyFragment
import com.example.eatraw.mypagefrg.MyreviewFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class MypageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMypageBinding
    private lateinit var nickhello: String

    //    private lateinit var email: TextView
    private lateinit var thumbnail: ImageView
    private lateinit var logoutTextView: TextView
//    private lateinit var userTextView: TextView

    private var user: FirebaseUser? = null


    private lateinit var adminTextView: TextView


    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        navController = nav_host_fragment.findNavController()

        val modifyLayout = findViewById<TextView>(R.id.modify)
        val reviewLayout = findViewById<TextView>(R.id.myreview)
        val alarmLayout = findViewById<TextView>(R.id.alarm)
        val centerLayout = findViewById<TextView>(R.id.center)
        val email = findViewById<TextView>(R.id.email)
        user = FirebaseAuth.getInstance().currentUser
        email.text = user?.email

        adminTextView = binding.admin


        modifyLayout.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ModifyFragment()).addToBackStack(null).commit()
        }
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
        logoutTextView = findViewById(R.id.logout)
        logoutTextView.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
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

        // 관리자페이지로 이동
        // 사용자가 관리자 권한을 가지고 있는지 확인하고, 관리자일 경우 AdminActivity로 이동합니다.
        if (user != null) {
            checkAdminPermission(user!!) { isAdmin ->
                if (isAdmin) {
                    // 사용자가 관리자인 경우에 수행할 작업
                    adminTextView.visibility = View.VISIBLE
                    adminTextView.setOnClickListener {
                        val intent = Intent(this@MypageActivity, AdminActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    // 사용자가 관리자가 아닌 경우에 수행할 작업
                    adminTextView.visibility = View.GONE
                }
            }

        }

    }

    private fun checkAdminPermission(user: FirebaseUser, callback: (Boolean) -> Unit) {
        val currentUserUid = user.uid

        // Firestore에 접근하여 사용자의 관리자 여부를 확인
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("users")

        // 현재 사용자의 UID를 사용하여 해당 사용자의 정보를 가져옴
        usersCollection.document(currentUserUid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    // 'isAdmin' 필드 값을 확인하여 관리자인지 아닌지 판별
                    val isAdmin = document.getBoolean("admin") ?: false
                    // 관리자 여부에 따라 true 또는 false를 콜백 함수로 전달
                    callback(isAdmin)
                } else {
                    // 문서가 존재하지 않을 때, 일반 사용자로 처리
                    callback(false)
                }
            }
            .addOnFailureListener { exception ->
                // 데이터를 가져오지 못한 경우 예외 처리
                callback(false)
            }
    }


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}