package com.example.eatraw

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.firebase.firestore.FirebaseFirestore

class QuoteActivity : AppCompatActivity() {
    private lateinit var barChart: BarChart

    private val fishKinds = listOf("광어", "우럭", "참돔", "돌돔","감성돔")

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quote)
        val marketAverage = findViewById<TextView>(R.id.marketAverage)

        barChart = findViewById(R.id.barChart)

        // 인텐트로 전달된 시장 이름 가져오기
        val marketName = intent.getStringExtra("marketName")
        marketAverage.text = marketName +" 어종별 시세"
        if (!marketName.isNullOrBlank()) {
            loadAndDisplayFishKindAveragesForMarket(marketName)
        }
    }

    private fun loadAndDisplayFishKindAveragesForMarket(marketName: String) {
        val firestore = FirebaseFirestore.getInstance()
        val fishKindAverages = mutableMapOf<String, Float>()

        for (fishKind in fishKinds) {
            firestore.collection("review")
                .whereEqualTo("marketName", marketName)
                .whereEqualTo("fishKind", fishKind)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    var totalCost = 0.0
                    var count = 0

                    for (document in querySnapshot) {
                        val cost = document.getDouble("cost")
                        if (cost != null) {
                            totalCost += cost
                            count++
                        }
                    }

                    if (count > 0) {
                        val averageCost = totalCost.toFloat() / count
                        fishKindAverages[fishKind] = averageCost
                    }

                    if (fishKindAverages.size == fishKinds.size) {
                        // 모든 어종에 대한 데이터를 로드한 후 차트를 표시합니다.
                        displayFishKindAveragesChart(fishKindAverages)
                    }
                }
                .addOnFailureListener { exception ->
                    // 오류 처리
                }
        }
    }

    private fun displayFishKindAveragesChart(fishKindAverages: Map<String, Float>) {
        if (fishKindAverages.isEmpty()) {
            // 데이터가 없을 때 메시지를 표시하는 처리
            val noDataMessage = "현재 수치화하기 위한 데이터량이 부족합니다"
            val noDataTextView = TextView(this)
            noDataTextView.text = noDataMessage
            noDataTextView.textSize = 16f
            noDataTextView.gravity = Gravity.CENTER
            barChart.clear() // 기존 데이터 및 내용 제거
            barChart.addView(noDataTextView) // 메시지를 나타내는 TextView를 추가
            barChart.invalidate()
        } else {
            val barEntries = mutableListOf<BarEntry>()
            val labels = mutableListOf<String>()

            for ((index, fishKind) in fishKinds.withIndex()) {
                val averageCost = fishKindAverages[fishKind] ?: 0.0f
                barEntries.add(BarEntry(index.toFloat(), averageCost))
                labels.add(fishKind)
            }

            val barDataSet = BarDataSet(barEntries, "어종별 평균 시세")

            val dataSets: ArrayList<IBarDataSet> = ArrayList()
            dataSets.add(barDataSet)

            val data = BarData(dataSets)
            data.setValueTextSize(20f) // 막대에 레이블 텍스트 크기 설정
            data.barWidth = 0.4f // 막대 너비 조정

            barChart.clear() // 기존 데이터 및 내용 제거
            barChart.data = data
            barChart.description.isEnabled = false
            barChart.setFitBars(true)
            barChart.animateY(1000)

            // 막대 레이블 설정
            val xAxis = barChart.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.setGranularity(1f)
            xAxis.textSize = 16f

            barChart.invalidate()
        }
    }



}
