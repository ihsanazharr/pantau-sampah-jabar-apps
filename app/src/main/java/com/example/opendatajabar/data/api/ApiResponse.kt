package com.example.opendatajabar.data.api

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("message") val message: String,
    @SerializedName("error") val error: Int,
    @SerializedName("data") val data: List<ApiDataItem> // Gunakan model terpisah!
)

data class ApiDataItem(
    @SerializedName("kode_kabupaten_kota") val kodeKabupatenKota: String,
    @SerializedName("kode_provinsi") val kodeProvinsi: String,
    @SerializedName("nama_kabupaten_kota") val namaKabupatenKota: String,
    @SerializedName("nama_provinsi") val namaProvinsi: String,
    @SerializedName("jumlah_produksi_sampah") val rataRataLamaSekolah: Double,
    @SerializedName("satuan") val satuan: String,
    @SerializedName("tahun") val tahun: Int
)
