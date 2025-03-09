package com.example.opendatajabar.viewmodel

import androidx.lifecycle.*
import com.example.opendatajabar.data.local.DataEntity
import com.example.opendatajabar.data.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    private val repository: DataRepository // ✅ Hanya gunakan repository
) : ViewModel() {

    val dataList: LiveData<List<DataEntity>> = repository.getAllData().asLiveData()

    private val _rowCount = MutableLiveData<Int>()
    val rowCount: LiveData<Int> get() = _rowCount

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _apiError = MutableLiveData<String?>()
    val apiError: LiveData<String?> = _apiError

    init {
        fetchRowCount()
        fetchDataFromApi()
    }

    fun fetchRowCount() {
        viewModelScope.launch(Dispatchers.IO) {
            val count = repository.getRowCount()
            withContext(Dispatchers.Main) {
                _rowCount.value = count
            }
        }
    }

    fun fetchDataFromApi() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                val dataList = repository.fetchDataFromApi()
                _apiError.postValue(null)
            } catch (e: Exception) {
                _apiError.postValue("Failed to fetch data: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }

    }

    fun insertData(data: DataEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(data)
            fetchRowCount()
        }
    }

    fun updateData(data: DataEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(data)
        }
    }

    fun deleteData(data: DataEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteData(data)
            fetchRowCount()
        }
    }

    suspend fun getDataById(id: Int): DataEntity? {
        return withContext(Dispatchers.IO) {
            repository.getDataById(id)
        }
    }
}
