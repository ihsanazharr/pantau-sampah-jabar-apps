package com.example.opendatajabar.ui.screen.dataEntry

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.opendatajabar.data.local.DataEntity
import com.example.opendatajabar.ui.theme.GradientBackground
import com.example.opendatajabar.viewmodel.DataViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    onClose: () -> Unit
) {

    val viewModel: DataViewModel = hiltViewModel()
    val context = LocalContext.current
    val dataList by viewModel.dataList.observeAsState(initial = emptyList())

    var kodeProvinsi by remember { mutableStateOf("") }
    var namaProvinsi by remember { mutableStateOf("") }
    var kodeKabupatenKota by remember { mutableStateOf("") }
    var namaKabupatenKota by remember { mutableStateOf("") }
    var rataRataLamaSekolah by remember { mutableStateOf("") }
    var satuan by remember { mutableStateOf("") }
    var tahun by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var showBackDialog by remember { mutableStateOf(false) }
    var isDataLoaded by remember { mutableStateOf(false) }
    var isDataChanged by remember { mutableStateOf(false) }

    LaunchedEffect(dataList) {
        if (dataList.isNotEmpty()) {
            kodeProvinsi = dataList.first().kodeProvinsi.toString()
            namaProvinsi = dataList.first().namaProvinsi
        }
    }

    LaunchedEffect(dataList) {
        if (dataList.isNotEmpty()) {
            kodeProvinsi = dataList.first().kodeProvinsi.toString()
            namaProvinsi = dataList.first().namaProvinsi
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Input Data") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (isDataChanged) {
                            showBackDialog = true
                        } else {
                            onClose()
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onTertiary

                )
            )
        }
    ) { paddingValues ->
        GradientBackground {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = kodeProvinsi,
                    onValueChange = { kodeProvinsi = it },
                    label = { Text("Kode Provinsi") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = namaProvinsi,
                    onValueChange = { namaProvinsi = it },
                    label = { Text("Nama Provinsi") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = kodeKabupatenKota,
                    onValueChange = { kodeKabupatenKota = it },
                    label = { Text("Kode Kabupaten/Kota") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = namaKabupatenKota,
                    onValueChange = { namaKabupatenKota = it },
                    label = { Text("Nama Kabupaten/Kota") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = rataRataLamaSekolah,
                    onValueChange = { rataRataLamaSekolah = it },
                    label = { Text("Jumlah Produksi Sampah") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = satuan,
                    onValueChange = { satuan = it },
                    label = { Text("Satuan") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = tahun,
                    onValueChange = { tahun = it },
                    label = { Text("Tahun") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (kodeProvinsi.isBlank() || namaProvinsi.isBlank() ||
                            kodeKabupatenKota.isBlank() || namaKabupatenKota.isBlank() ||
                            rataRataLamaSekolah.isBlank() || satuan.isBlank() || tahun.isBlank()
                        ) {
                            showDialog = true
                        } else {
                            try {
                                val data = DataEntity(
                                    kodeProvinsi = kodeProvinsi,
                                    namaProvinsi = namaProvinsi,
                                    kodeKabupatenKota = kodeKabupatenKota,
                                    namaKabupatenKota = namaKabupatenKota,
                                    rataRataLamaSekolah = rataRataLamaSekolah.toDouble(),
                                    satuan = satuan,
                                    tahun = tahun
                                )
                                viewModel.insertData(data)
                                Toast.makeText(
                                    context,
                                    "Data berhasil ditambahkan",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onClose()
                            } catch (e: NumberFormatException) {
                                Toast.makeText(
                                    context,
                                    "Harap masukkan angka yang valid",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Simpan Data")
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(
                        onClick = { showDialog = false }
                    ) {
                        Text("OK")
                    }
                },
                title = { Text("Input Tidak Lengkap") },
                text = { Text("Harap isi semua data sebelum menyimpan!") }
            )
        }

        if (showBackDialog) {
            AlertDialog(
                onDismissRequest = { showBackDialog = false },
                confirmButton = {
                    Button(
                        onClick = {
                            showBackDialog = false

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Ya")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showBackDialog = false }
                    ) {
                        Text("Tidak")
                    }
                },
                title = { Text("Konfirmasi") },
                text = { Text("Apakah Anda yakin untuk membatalkan perubahan?") }
            )
        }

    }
}