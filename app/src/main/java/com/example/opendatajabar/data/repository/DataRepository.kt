package com.example.opendatajabar.data.repository

import com.example.opendatajabar.data.api.ApiService
import com.example.opendatajabar.data.local.DataDao
import com.example.opendatajabar.data.local.DataEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val dataDao: DataDao,
    private val apiService: ApiService
) {
    fun getAllData(): Flow<List<DataEntity>> = dataDao.getAllData()

    suspend fun fetchDataFromApi(): List<DataEntity> {
        return try {
            val response = apiService.getData()
            response.data.map { apiItem ->
                DataEntity(
                    kodeKabupatenKota = apiItem.kodeKabupatenKota,
                    kodeProvinsi = apiItem.kodeProvinsi,
                    namaKabupatenKota = apiItem.namaKabupatenKota,
                    namaProvinsi = apiItem.namaProvinsi,
                    rataRataLamaSekolah = apiItem.rataRataLamaSekolah,
                    satuan = apiItem.satuan,
                    tahun = apiItem.tahun.toString()
                )
            }.also { dataList ->
                dataDao.insertAll(dataList) // ✅ Simpan ke database
            }
        } catch (e: Exception) {
            throw Exception("Gagal mengambil data dari API: ${e.message}")
        }
    }

    suspend fun insertData(data: DataEntity) {
        dataDao.insertData(data)
    }

    suspend fun insertAll(dataList: List<DataEntity>) {
        dataDao.insertAll(dataList)
    }

    suspend fun updateData(data: DataEntity) {
        dataDao.updateData(data)
    }

    suspend fun deleteData(data: DataEntity) {
        dataDao.deleteData(data)
    }

    suspend fun getRowCount(): Int {
        return dataDao.getRowCount()
    }

    suspend fun getDataById(id: Int): DataEntity? {
        return dataDao.getDataById(id)
    }
}
