package com.ayusutra.app.ui.tracking

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayusutra.app.R
import com.ayusutra.app.databinding.ActivityTherapyTrackingBinding
import com.ayusutra.app.models.SymptomReport
import com.ayusutra.app.models.TherapySession
import com.ayusutra.app.models.User
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class TherapyTrackingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTherapyTrackingBinding
    private lateinit var adapter: TherapyTrackingAdapter
    private val therapySessions = mutableListOf<TherapySession>()
    private val symptomReports = mutableListOf<SymptomReport>()
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTherapyTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupRecyclerView()
        loadDummyData()
        updateProgressSummary()
        setupChart()
        setupChipListeners()
        setupFab()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Therapy Tracking"
        }
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }
    
    private fun setupRecyclerView() {
        adapter = TherapyTrackingAdapter(therapySessions) { therapySession ->
            // Handle therapy session click
            // Could navigate to a detailed view
        }
        binding.rvRecentTherapies.layoutManager = LinearLayoutManager(this)
        binding.rvRecentTherapies.adapter = adapter
    }
    
    private fun loadDummyData() {
        // Create dummy user
        val user = User(
            id = "U123456",
            name = "Rahul Sharma",
            email = "rahul.sharma@example.com",
            phone = "+91 9876543210",
            uhid = "AYUR123456",
            age = 35,
            gender = "Male",
            address = "123 Main Street, Mumbai"
        )
        
        // Create dummy therapy sessions
        val calendar = Calendar.getInstance()
        
        // Add symptom reports for the past 30 days
        for (i in 30 downTo 0 step 5) {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_MONTH, -i)
            
            symptomReports.add(
                SymptomReport(
                    id = "SR${100 + i}",
                    date = calendar.time,
                    painLevel = (3 + (i % 5)).coerceAtMost(10),
                    mobilityLevel = (5 + (i % 3)).coerceAtMost(10),
                    sleepQuality = (4 + (i % 4)).coerceAtMost(10),
                    stressLevel = (8 - (i % 5)).coerceAtLeast(1),
                    notes = if (i % 10 == 0) "Feeling much better after therapy session" else "",
                    userId = user.id
                )
            )
        }
        
        // Add therapy sessions
        val therapyTypes = listOf("Abhyanga", "Shirodhara", "Nasya", "Basti", "Swedana")
        val practitioners = listOf("Dr. Anand Mishra", "Dr. Priya Patel", "Dr. Vikram Singh")
        
        for (i in 0 until 5) {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_MONTH, -(i * 7))
            
            therapySessions.add(
                TherapySession(
                    id = "TS${100 + i}",
                    therapyName = therapyTypes[i % therapyTypes.size],
                    date = calendar.time,
                    practitioner = practitioners[i % practitioners.size],
                    effectivenessRating = (7 + (i % 4)).coerceAtMost(10),
                    comfortRating = (6 + (i % 5)).coerceAtMost(10),
                    symptomChange = if (i == 0) "Moderate improvement" else if (i == 1) "Significant improvement" else "Slight improvement",
                    notes = "Regular session with focus on ${if (i % 2 == 0) "upper back" else "lower back"} pain",
                    userId = user.id
                )
            )
        }
        
        adapter.notifyDataSetChanged()
    }
    
    private fun updateProgressSummary() {
        // Calculate average pain reduction
        if (symptomReports.size >= 2) {
            val firstReport = symptomReports.last()
            val lastReport = symptomReports.first()
            
            val painReduction = firstReport.painLevel - lastReport.painLevel
            val mobilityImprovement = lastReport.mobilityLevel - firstReport.mobilityLevel
            val sleepImprovement = lastReport.sleepQuality - firstReport.sleepQuality
            val stressReduction = firstReport.stressLevel - lastReport.stressLevel
            
            binding.tvPainReduction.text = "${painReduction > 0 ? "+" : ""}${painReduction}"
            binding.tvMobilityImprovement.text = "${mobilityImprovement > 0 ? "+" : ""}${mobilityImprovement}"
            binding.tvSleepImprovement.text = "${sleepImprovement > 0 ? "+" : ""}${sleepImprovement}"
            binding.tvStressReduction.text = "${stressReduction > 0 ? "+" : ""}${stressReduction}"
            
            // Update progress bars
            binding.progressPain.progress = (10 - lastReport.painLevel) * 10
            binding.progressMobility.progress = lastReport.mobilityLevel * 10
            binding.progressSleep.progress = lastReport.sleepQuality * 10
            binding.progressStress.progress = (10 - lastReport.stressLevel) * 10
        }
    }
    
    private fun setupChart() {
        // Find the chart view from the layout
        val chartContainer = binding.chartContainer
        
        // Create a new LineChart
        val chart = LineChart(this)
        chart.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        
        // Add the chart to the container
        chartContainer.removeAllViews()
        chartContainer.addView(chart)
        
        // Configure chart appearance
        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setPinchZoom(true)
        chart.setDrawGridBackground(false)
        chart.setBackgroundColor(Color.TRANSPARENT)
        
        // Configure X axis
        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        
        // Configure left Y axis
        val leftAxis = chart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = 10f
        
        // Configure right Y axis
        val rightAxis = chart.axisRight
        rightAxis.isEnabled = false
        
        // Update chart with data
        updateChartData(chart)
    }
    
    private fun updateChartData(chart: LineChart) {
        // Get selected symptoms from chip group
        val showPain = binding.chipPain.isChecked
        val showMobility = binding.chipMobility.isChecked
        val showSleep = binding.chipSleep.isChecked
        val showStress = binding.chipStress.isChecked
        
        val dataSets = ArrayList<LineDataSet>()
        val dateLabels = ArrayList<String>()
        
        // Sort reports by date (oldest to newest)
        val sortedReports = symptomReports.sortedBy { it.date }
        
        // Create date labels for x-axis
        val shortDateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
        sortedReports.forEach { report ->
            dateLabels.add(shortDateFormat.format(report.date))
        }
        
        // Set x-axis labels
        chart.xAxis.valueFormatter = IndexAxisValueFormatter(dateLabels)
        
        // Create data entries for each symptom type
        if (showPain) {
            val entries = ArrayList<Entry>()
            sortedReports.forEachIndexed { index, report ->
                entries.add(Entry(index.toFloat(), report.painLevel.toFloat()))
            }
            
            val dataSet = LineDataSet(entries, "Pain Level")
            dataSet.color = Color.RED
            dataSet.setCircleColor(Color.RED)
            dataSet.lineWidth = 2f
            dataSet.circleRadius = 3f
            dataSet.setDrawCircleHole(false)
            dataSets.add(dataSet)
        }
        
        if (showMobility) {
            val entries = ArrayList<Entry>()
            sortedReports.forEachIndexed { index, report ->
                entries.add(Entry(index.toFloat(), report.mobilityLevel.toFloat()))
            }
            
            val dataSet = LineDataSet(entries, "Mobility")
            dataSet.color = Color.GREEN
            dataSet.setCircleColor(Color.GREEN)
            dataSet.lineWidth = 2f
            dataSet.circleRadius = 3f
            dataSet.setDrawCircleHole(false)
            dataSets.add(dataSet)
        }
        
        if (showSleep) {
            val entries = ArrayList<Entry>()
            sortedReports.forEachIndexed { index, report ->
                entries.add(Entry(index.toFloat(), report.sleepQuality.toFloat()))
            }
            
            val dataSet = LineDataSet(entries, "Sleep Quality")
            dataSet.color = Color.BLUE
            dataSet.setCircleColor(Color.BLUE)
            dataSet.lineWidth = 2f
            dataSet.circleRadius = 3f
            dataSet.setDrawCircleHole(false)
            dataSets.add(dataSet)
        }
        
        if (showStress) {
            val entries = ArrayList<Entry>()
            sortedReports.forEachIndexed { index, report ->
                entries.add(Entry(index.toFloat(), report.stressLevel.toFloat()))
            }
            
            val dataSet = LineDataSet(entries, "Stress Level")
            dataSet.color = Color.MAGENTA
            dataSet.setCircleColor(Color.MAGENTA)
            dataSet.lineWidth = 2f
            dataSet.circleRadius = 3f
            dataSet.setDrawCircleHole(false)
            dataSets.add(dataSet)
        }
        
        // Create and set the data
        val lineData = LineData(dataSets)
        chart.data = lineData
        
        // Refresh the chart
        chart.invalidate()
    }
    
    private fun setupChipListeners() {
        // Get chart view
        val chartContainer = binding.chartContainer
        val chart = if (chartContainer.childCount > 0) chartContainer.getChildAt(0) as? LineChart else null
        
        // Set up listeners for filter chips
        binding.chipPain.setOnCheckedChangeListener { _, _ ->
            chart?.let { updateChartData(it) }
        }
        
        binding.chipMobility.setOnCheckedChangeListener { _, _ ->
            chart?.let { updateChartData(it) }
        }
        
        binding.chipSleep.setOnCheckedChangeListener { _, _ ->
            chart?.let { updateChartData(it) }
        }
        
        binding.chipStress.setOnCheckedChangeListener { _, _ ->
            chart?.let { updateChartData(it) }
        }
    }
    
    private fun setupFab() {
        binding.fab.setOnClickListener {
            showSymptomReportDialog()
        }
    }
    
    private fun showSymptomReportDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_symptom_report, null)
        dialog.setContentView(view)
        
        val etDate = view.findViewById<TextInputEditText>(R.id.et_date)
        val seekBarPain = view.findViewById<Slider>(R.id.seek_bar_pain)
        val tvPainValue = view.findViewById<TextView>(R.id.tv_pain_value)
        val seekBarMobility = view.findViewById<Slider>(R.id.seek_bar_mobility)
        val tvMobilityValue = view.findViewById<TextView>(R.id.tv_mobility_value)
        val seekBarSleep = view.findViewById<Slider>(R.id.seek_bar_sleep)
        val tvSleepValue = view.findViewById<TextView>(R.id.tv_sleep_value)
        val seekBarStress = view.findViewById<Slider>(R.id.seek_bar_stress)
        val tvStressValue = view.findViewById<TextView>(R.id.tv_stress_value)
        val etNotes = view.findViewById<TextInputEditText>(R.id.et_notes)
        val btnCancel = view.findViewById<MaterialButton>(R.id.btn_cancel)
        val btnSave = view.findViewById<MaterialButton>(R.id.btn_save)
        
        // Set current date
        val calendar = Calendar.getInstance()
        etDate.setText(dateFormat.format(calendar.time))
        
        // Setup date picker
        etDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    etDate.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
        
        // Setup seekbar listeners
        seekBarPain.addOnChangeListener { _, value, _ ->
            val intValue = value.toInt()
            tvPainValue.text = "$intValue/10"
        }
        
        seekBarMobility.addOnChangeListener { _, value, _ ->
            val intValue = value.toInt()
            tvMobilityValue.text = "$intValue/10"
        }
        
        seekBarSleep.addOnChangeListener { _, value, _ ->
            val intValue = value.toInt()
            tvSleepValue.text = "$intValue/10"
        }
        
        seekBarStress.addOnChangeListener { _, value, _ ->
            val intValue = value.toInt()
            tvStressValue.text = "$intValue/10"
        }
        
        // Setup button listeners
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        
        btnSave.setOnClickListener {
            // Create new symptom report
            val newReport = SymptomReport(
                id = "SR${System.currentTimeMillis()}",
                date = calendar.time,
                painLevel = seekBarPain.value.toInt(),
                mobilityLevel = seekBarMobility.value.toInt(),
                sleepQuality = seekBarSleep.value.toInt(),
                stressLevel = seekBarStress.value.toInt(),
                notes = etNotes.text.toString(),
                userId = "U123456" // Using dummy user ID
            )
            
            // Add to list and update UI
            symptomReports.add(0, newReport)
            updateProgressSummary()
            
            // Update chart with new data
            val chartContainer = binding.chartContainer
            val chart = if (chartContainer.childCount > 0) chartContainer.getChildAt(0) as? LineChart else null
            chart?.let { updateChartData(it) }
            
            dialog.dismiss()
        }
        
        dialog.show()
    }
}