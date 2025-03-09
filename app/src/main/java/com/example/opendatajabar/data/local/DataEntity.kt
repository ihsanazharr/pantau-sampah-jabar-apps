package com.example.opendatajabar.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "data_table")
data class DataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @SerializedName("kode_provinsi")
    val kodeProvinsi: String,

    @SerializedName("nama_provinsi")
    val namaProvinsi: String,

    @SerializedName("kode_kabupaten_kota")
    val kodeKabupatenKota: String,

    @SerializedName("nama_kabupaten_kota")
    val namaKabupatenKota: String,

    @SerializedName("jumlah_produksi_sampah")
    val rataRataLamaSekolah: Double,

    @SerializedName("satuan")
    val satuan: String,

    @SerializedName("tahun")
    val tahun: String
)