package com.example.eatraw

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatraw.adapter.ComparingPriceAdapter
import com.example.eatraw.data.ComparingPriceItem

class ComparingPriceListActivity : AppCompatActivity() {
//    private val TAG = this.javaClass.simpleName
//    //콜백 인스턴스 생성
//    private val callback = object : OnBackPressedCallback(true) {
//        override fun handleOnBackPressed() {
//            // 뒤로 버튼 이벤트 처리
//            Log.e(TAG, "뒤로가기 클릭")
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comparing_price_list)

        val comparingPriceItems = intent.getSerializableExtra("comparingPriceItems")?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it as? ArrayList<ComparingPriceItem>
            } else {
                it as? ArrayList<ComparingPriceItem>
            }
        }

        // RecyclerView와 어댑터 설정
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewComparingPrice)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = comparingPriceItems?.let { ComparingPriceAdapter(it) }

        val backButton = findViewById<ImageView>(R.id.img_backarrow)
        backButton.setOnClickListener{
//            this.onBackPressedDispatcher.addCallback(this, callback)
            onBackPressed()
        }
    }
}
