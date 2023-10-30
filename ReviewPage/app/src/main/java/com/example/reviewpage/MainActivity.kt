package com.example.reviewpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.reviewpage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // ActivityMainBinding을 클래스 멤버로 만듭니다.
    private lateinit var binding: ActivityMainBinding
    private lateinit var spinner: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var spinner3: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ActivityMainBinding을 초기화합니다.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Spinner 및 TextView를 초기화합니다.
        spinner = binding.spinner1
        spinner2 =binding.spinner2

        // Spinner에 데이터를 설정합니다.
            val items = arrayOf("서울", "부산", "대구","경남","경북","충남")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(1)

            val items2 = arrayOf("자갈치수산", "은하수산", "대명수산","우리수산","부산수산","미리수산","양어수산")
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, items2)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = adapter2
        spinner2.setSelection(0)



    }
}
