package com.example.opendatajabar.data.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.opendatajabar.data.api.ApiService
import com.example.opendatajabar.data.local.AppDatabase
import com.example.opendatajabar.data.local.DataDao
import com.example.opendatajabar.data.repository.DataRepository
import com.example.opendatajabar.viewmodel.DataViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://data.jabarprov.go.id/api-backend/bigdata/disperkim/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://data.jabarprov.go.id/api-backend/bigdata/disperkim/") // ✅ API URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "open_data_jabar.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDataDao(database: AppDatabase): DataDao {
        return database.dataDao()
    }

    @Provides
    @Singleton
    fun provideDataRepository(dataDao: DataDao, apiService: ApiService): DataRepository {
        return DataRepository(dataDao, apiService)
    }

    @Provides
    fun provideDataViewModel(repository: DataRepository): DataViewModel {
        return DataViewModel(repository)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

}
