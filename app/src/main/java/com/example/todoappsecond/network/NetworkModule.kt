package com.example.todoappsecond.network

import com.example.todoappsecond.data.api.TodoApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://hive.mrdekk.ru/todo/"
private const val AUTH_TOKEN = "Orophin"

object NetworkModule {

    private val authInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $AUTH_TOKEN")
            .build()
        chain.proceed(request)
    }

    // Ф-я для получения экземпляра TodoApiService
    fun provideTodoApiService(): TodoApiService {
        val client = OkHttpClient.Builder()
            .hostnameVerifier { _, _ -> true }
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(TodoApiService::class.java)
    }

}
