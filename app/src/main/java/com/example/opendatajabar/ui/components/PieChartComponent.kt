package com.example.opendatajabar.ui.components

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import com.example.opendatajabar.data.local.DataEntity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight

@Composable
fun PieChartComponent(dataList: List<DataEntity>) {
    val dataByCity = remember(dataList) {
        dataList.groupBy { it.namaKabupatenKota }.mapValues { it.value.size }
    }

    var showDialog by remember { mutableStateOf(false) }
    var dialogCity by remember { mutableStateOf("") }
    var dialogCount by remember { mutableStateOf(0) }

    if (dataByCity.isNotEmpty()) {
        AndroidView(
            factory = { context ->
                PieChart(context).apply {
                    val entries = dataByCity.map { (city, count) -> PieEntry(count.toFloat(), city) }
                    val dataSet = PieDataSet(entries, "Produksi Sampah")
                    dataSet.colors = listOf(
                        Color.rgb(255, 102, 0), Color.rgb(255, 170, 0),
                        Color.rgb(255, 255, 0), Color.rgb(170, 255, 0),
                        Color.rgb(0, 255, 170), Color.rgb(0, 170, 255),
                        Color.rgb(170, 0, 255), Color.rgb(255, 0, 170)
                    )
                    data = PieData(dataSet)
                    description.isEnabled = false
                    setUsePercentValues(true)
                    legend.isEnabled = true

                    setOnChartValueSelectedListener(object : com.github.mikephil.charting.listener.OnChartValueSelectedListener {
                        override fun onValueSelected(e: com.github.mikephil.charting.data.Entry?, h: Highlight?) {
                            e?.let {
                                val pieEntry = it as PieEntry
                                dialogCity = pieEntry.label
                                dialogCount = pieEntry.value.toInt()
                                showDialog = true
                            }
                        }

                        override fun onNothingSelected() {}
                    })

                    invalidate()
                }
            },
            modifier = androidx.compose.ui.Modifier.fillMaxSize()
        )
    } else {
        Text("Tidak ada data untuk ditampilkan.")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Informasi Kota") },
            text = { Text("Kota: $dialogCity\nJumlah: $dialogCount") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}