package com.example.eatraw

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.eatraw.databinding.ActivityMypageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MypageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMypageBinding
    private lateinit var nickhello: String
    private lateinit var email: String
    private lateinit var thumbnail: ImageView
    lateinit var navController: NavController







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        navController = nav_host_fragment.findNavController()


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
}