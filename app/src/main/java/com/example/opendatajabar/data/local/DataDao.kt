package com.example.opendatajabar.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: DataEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dataList: List<DataEntity>)

    @Update
    suspend fun update(data: DataEntity)

    @Query("SELECT * FROM data_table ORDER BY id DESC")
    suspend fun getAll(): List<DataEntity>

    @Query("SELECT * FROM data_table WHERE id = :dataId")
    suspend fun getById(dataId: Int): DataEntity?

    @Query("SELECT COUNT(*) FROM data_table")
    suspend fun getCount(): Int


        // Mengambil semua data dari database sebagai Flow
        @Query("SELECT * FROM data_table")
        fun getAllData(): Flow<List<DataEntity>>

        // Mengambil jumlah total data dalam database
        @Query("SELECT COUNT(*) FROM data_table")
        suspend fun getRowCount(): Int

        // Mengambil data berdasarkan ID
        @Query("SELECT * FROM data_table WHERE id = :id")
        suspend fun getDataById(id: Int): DataEntity?

        // Menyisipkan satu data ke dalam database
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertData(data: DataEntity)


        // Memperbarui data yang sudah ada
        @Update
        suspend fun updateData(data: DataEntity)

        // Menghapus satu data dari database
        @Delete
        suspend fun deleteData(data: DataEntity)


}