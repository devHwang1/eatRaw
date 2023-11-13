package com.example.eatraw

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import java.util.Calendar

class QuoteActivity : AppCompatActivity() {
    private lateinit var barChart: BarChart
    private lateinit var textViewq: TextView

    private val fishKinds = listOf( "우럭", "참돔", "방어", "전복", "돌돔")

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quote)

        barChart = findViewById(R.id.barChart)

        val spinner: Spinner = findViewById(R.id.spinner)
        val periods = arrayOf("1주일 내", "1달 내", "1년 내")
        textViewq = findViewById(R.id.textViewq)

        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, periods)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long) {
                val calendar = Calendar.getInstance()
                val endTimestamp = Timestamp.now()
                when (position) {
                    0 -> {
                        calendar.add(Calendar.WEEK_OF_YEAR, -1)
                    }
                    1 -> {
                        calendar.add(Calendar.MONTH, -1)
                    }
                    2 -> {
                        calendar.add(Calendar.YEAR, -1)
                    }
                }
                val startTimestamp = Timestamp(calendar.time)
                val likeMarket = intent.getStringExtra("likeMarket")
                val marketName = intent.getStringExtra("marketName")
                if (!marketName.isNullOrBlank()) {
                    loadAndDisplayFishKindAveragesForMarket(marketName, startTimestamp, endTimestamp)
                }
                if (likeMarket != null) {
                    textViewq.text = "$likeMarket 어종별 시세"
                    loadAndDisplayFishKindAveragesForMarket2(likeMarket, startTimestamp, endTimestamp)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }
    }

    private fun loadAndDisplayFishKindAveragesForMarket(marketName: String, startTimestamp: Timestamp, endTimestamp: Timestamp) {
        val firestore = FirebaseFirestore.getInstance()
        val fishKindAverages = mutableMapOf<String, Float>()
        Log.w("시간값알기", "$startTimestamp")
        Log.w("시간값알기", "$endTimestamp")
        Log.w("시간값알기", "$marketName")
        textViewq.text = "$marketName" + " 어종별 시세"
        for (fishKind in fishKinds) {
            firestore.collection("review")
                .whereEqualTo("marketName", marketName)
                .whereEqualTo("fishKind", fishKind)
                .whereGreaterThanOrEqualTo("timestamp", startTimestamp)
                .whereLessThanOrEqualTo("timestamp", endTimestamp)
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
                        Log.w("평균값알기", "$averageCost")
                        Log.w("평균값알기", "$totalCost")
                        Log.w("평균값알기", "$fishKindAverages")
                    }

                    if (fishKindAverages.size == fishKinds.size) {
                        displayFishKindAveragesChart(fishKindAverages)
                    }
                }
                .addOnFailureListener { exception ->
                }
        }
    }
    private fun loadAndDisplayFishKindAveragesForMarket2(likeMarket: String, startTimestamp: Timestamp, endTimestamp: Timestamp) {
        val firestore = FirebaseFirestore.getInstance()
        val fishKindAverages = mutableMapOf<String, Float>()
        Log.w("시간값알기", "$startTimestamp")
        Log.w("시간값알기", "$endTimestamp")
        Log.w("시간값알기", "$likeMarket")
        textViewq.text = "$likeMarket" + " 어종별 시세"
        for (fishKind in fishKinds) {
            firestore.collection("review")
                .whereEqualTo("marketName", likeMarket)
                .whereEqualTo("fishKind", fishKind)
                .whereGreaterThanOrEqualTo("timestamp", startTimestamp)
                .whereLessThanOrEqualTo("timestamp", endTimestamp)
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
                        Log.w("평균값알기", "$averageCost")
                        Log.w("평균값알기", "$totalCost")
                        Log.w("평균값알기", "$fishKindAverages")
                    }

                    if (fishKindAverages.size == fishKinds.size) {
                        displayFishKindAveragesChart(fishKindAverages)
                    }
                }
                .addOnFailureListener { exception ->
                }
        }
    }

    private fun displayFishKindAveragesChart(fishKindAverages: Map<String, Float>) {
        val barEntries = mutableListOf<BarEntry>()
        val labels = mutableListOf<String>()

        for ((index, fishKind) in fishKinds.withIndex()) {
            val averageCost = fishKindAverages[fishKind] ?: 0.0f
            barEntries.add(BarEntry(index.toFloat(), averageCost))
            labels.add(fishKind)
        }

        val colors = intArrayOf(Color.rgb(197,255,140), Color.rgb(255,247,139)
                + Color.rgb(255, 211, 140),Color.rgb(140, 235, 255),Color.rgb(255, 142, 155)) // 막대의 갯수에 따라서 원하는 색상을 추가하세요

        val barDataSet = BarDataSet(barEntries, "어종별 평균 시세")
        barDataSet.setColors(*colors)

        val dataSets: ArrayList<IBarDataSet> = ArrayList()
        dataSets.add(barDataSet)

        val data = BarData(dataSets)
        data.setValueTextSize(15f)
        data.barWidth = 0.4f

        barChart.data = data
        barChart.description.isEnabled = false
        barChart.setFitBars(true)
        barChart.animateY(1000)
        barChart.setDrawGridBackground(false)

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setGranularity(1f)
        xAxis.textSize = 12f

        barChart.invalidate()
    }
}