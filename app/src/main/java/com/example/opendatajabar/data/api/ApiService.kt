package com.example.opendatajabar.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("od_16985_jumlah_produksi_sampah_berdasarkan_kabupatenkota_v3")
    suspend fun getData(): ApiResponse

}

fun create(): ApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://data.jabarprov.go.id/api-backend/bigdata/disperkim/") // Ganti dengan URL API yang benar
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(ApiService::class.java)
}
