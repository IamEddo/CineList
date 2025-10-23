package com.example.cinelist.di

import android.content.Context
import androidx.room.Room
import com.example.cinelist.data.MovieRepository
import com.example.cinelist.data.local.AppDatabase
import com.example.cinelist.data.local.CineListDao
import com.example.cinelist.data.remote.OmdbApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // !!!!! IMPORTANTE !!!!!
    // !!!!! COLOQUE SUA CHAVE DA API OMDb AQUI !!!!!
    private const val API_KEY = "SUA_API_KEY_AQUI"
    private const val BASE_URL = "https://www.omdbapi.com/"

    @Provides
    @Singleton
    fun provideApiKey(): String = API_KEY

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cinelist_db"
        )
            .fallbackToDestructiveMigration() // Em um app real, use migração
            .build()
    }

    @Provides
    @Singleton
    fun provideCineListDao(db: AppDatabase): CineListDao = db.cineListDao()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        // Interceptor para loggar as chamadas de API (útil para debug)
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOmdbApi(retrofit: Retrofit): OmdbApi = retrofit.create(OmdbApi::class.java)

    @Provides
    @Singleton
    fun provideMovieRepository(dao: CineListDao, api: OmdbApi, apiKey: String): MovieRepository {
        return MovieRepository(dao, api, apiKey)
    }
}