package com.example.opendatajabar.ui.screen.dataEntry

import android.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.opendatajabar.data.local.DataEntity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PieChartScreen(dataList: List<DataEntity>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Grafik Produksi Sampah (Per kota)",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PieChartComponent(dataList = dataList)
        }
    }
}

@Composable
fun PieChartComponent(dataList: List<DataEntity>) {
    val dataByCity = remember(dataList) {
        dataList.groupBy { it.namaKabupatenKota }
            .mapValues { entry -> entry.value.sumOf { it.rataRataLamaSekolah } }
    }

    var showDialog by remember { mutableStateOf(false) }
    var dialogCity by remember { mutableStateOf("") }
    var dialogCount by remember { mutableStateOf(0.0) }

    if (dataByCity.isNotEmpty()) {
        AndroidView(
            factory = { context ->
                PieChart(context).apply {
                    val entries = dataByCity.map { (city, count) -> PieEntry(count.toFloat(), city) }
                    val dataSet = PieDataSet(entries, "Produksi Sampah (Ton)")
                    dataSet.colors = listOf(
                        Color.rgb(153, 51, 0),
                        Color.rgb(153, 119, 0),
                        Color.rgb(153, 153, 0),
                        Color.rgb(119, 153, 0),
                        Color.rgb(0, 153, 119),
                        Color.rgb(0, 119, 153),
                        Color.rgb(119, 0, 153),
                        Color.rgb(153, 0, 119)
                    )
                    data = PieData(dataSet)
                    description.isEnabled = false
                    setUsePercentValues(false) // Ubah menjadi false
                    legend.isEnabled = true

                    // Format label pie chart
                    entries.forEach { entry ->
                        entry.label = "${entry.label} (${DecimalFormat("#,##0.00").format(entry.value)})"
                    }

                    setOnChartValueSelectedListener(object : com.github.mikephil.charting.listener.OnChartValueSelectedListener {
                        override fun onValueSelected(e: com.github.mikephil.charting.data.Entry?, h: Highlight?) {
                            e?.let {
                                val pieEntry = it as PieEntry
                                dialogCity = pieEntry.label
                                dialogCount = pieEntry.value.toDouble()
                                showDialog = true
                            }
                        }

                        override fun onNothingSelected() {}
                    })

                    invalidate()
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    } else {
        Text("Tidak ada data untuk ditampilkan.")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "Informasi Kota",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    )
                )
            },
            text = {
                Text(
                    text = "Kota: \n$dialogCity\n\nJumlah (Ton): \n${DecimalFormat("#,##0.00").format(dialogCount)}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Tutup")
                }
            }
        )
    }
}