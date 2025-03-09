package com.example.opendatajabar.ui.screen.home

import com.example.opendatajabar.ui.screen.dataEntry.InputScreen
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.opendatajabar.R
import com.example.opendatajabar.data.local.DataEntity
import com.example.opendatajabar.ui.screen.dataList.DeleteConfirmationDialog
import com.example.opendatajabar.ui.components.DataItemCard
import com.example.opendatajabar.ui.components.DropdownFilterTahun
import com.example.opendatajabar.ui.components.DropdownMenuFilter
import com.example.opendatajabar.ui.theme.GradientBackground
import com.example.opendatajabar.viewmodel.DataViewModel
import kotlinx.coroutines.launch

private const val TAG = "HomeScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: DataViewModel) {
    Log.d(TAG, "HomeScreen composable started")

    val dataList by viewModel.dataList.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<DataEntity?>(null) }
    var selectedFilter by remember { mutableStateOf("Semua") }
    var selectedFilterTahun by remember { mutableStateOf("Semua") }
    var currentPage by remember { mutableStateOf(0) }
    var showDataEntryScreen by remember { mutableStateOf(false) }

    val uniqueKabupatenKota = remember(dataList) {
        listOf("Semua") + dataList.map { it.namaKabupatenKota }.distinct()
    }
    val uniqueTahun = remember(dataList) {
        listOf("Semua") + dataList.map { it.tahun }.distinct()
    }

    val filteredData = remember(dataList, selectedFilter, selectedFilterTahun) {
        dataList.filter { item ->
            (selectedFilter == "Semua" || item.namaKabupatenKota == selectedFilter) &&
                    (selectedFilterTahun == "Semua" || item.tahun == selectedFilterTahun)
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        GradientBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Selamat datang!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 14.dp)
                )
                Text(
                    text = "Akses data penduduk Jawa Barat dengan mudah!",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 5.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inversePrimary),
                    onClick = { navController.navigate("input") }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Masukkan Data Baru + ",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 15.dp)
                ) {
                    Text(
                        text = "Pilihan Kab/Kota:",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    DropdownMenuFilter(
                        selectedFilter = selectedFilter,
                        options = uniqueKabupatenKota,
                        onFilterSelected = {
                            selectedFilter = it
                            currentPage = 0

                        }

                    )

                }

                Row(

                    verticalAlignment = Alignment.CenterVertically,

                    modifier = Modifier.padding(top = 15.dp, start = 20.dp)

                ) {

                    Text(
                        text = "Pilihan Tahun:",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    DropdownFilterTahun(
                        selectedFilterTahun = selectedFilterTahun,
                        options = uniqueTahun,
                        onFilterSelected = {
                            selectedFilterTahun = it
                            currentPage = 0
                        }
                    )
                }
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    filteredData.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = painterResource(id = R.drawable.confused),
                                    contentDescription = "Tidak ada data",
                                    modifier = Modifier.size(150.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Tidak ada data",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 23.sp
                                )
                            }
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
                        ) {
                            items(filteredData) { item ->
                                DataItemCard(
                                    item = item,
                                    onEditClick = { navController.navigate("edit/${item.id}") },
                                    onDeleteClick = {
                                        selectedItem = item
                                        showDialog = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDataEntryScreen) {
        InputScreen(
            onClose = { showDataEntryScreen = false }
        )
    }

    DeleteConfirmationDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onConfirm = {
            selectedItem?.let {
                viewModel.deleteData(it)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Data berhasil dihapus")
                }
            }
            showDialog = false
        }
    )
    }