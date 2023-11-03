package com.example.eatraw

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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

class MypageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMypageBinding
    private lateinit var nickhello: String
//    private lateinit var email: TextView
    private lateinit var thumbnail: ImageView
    private lateinit var logoutTextView: TextView
//    private lateinit var userTextView: TextView

    private var user: FirebaseUser? = null







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


        modifyLayout.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ModifyFragment()) .addToBackStack(null).commit()
        }
        reviewLayout.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MyreviewFragment()) .addToBackStack(null).commit()
        }
        alarmLayout.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, AlarmFragment()) .addToBackStack(null).commit()
        }
        centerLayout.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CenterFragment()) .addToBackStack(null).commit()
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
        bnv_main.run { setOnNavigationItemSelectedListener {
            when(it.itemId) {
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
                    val intent = Intent(this@MypageActivity, ComparingPriceListActivity::class.java)
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
}