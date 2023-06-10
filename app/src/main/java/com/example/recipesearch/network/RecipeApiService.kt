package com.example.recipesearch.network

import com.example.recipesearch.model.RecipeResult
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val RECIPE_API_BASE_URL = "https://recipe-search-api.vercel.app"

private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(RECIPE_API_BASE_URL)
    .client(okHttpClient)
    .build()

interface RecipeApiService {
    @GET("/recipes")
    suspend fun getRecipes(
        @Query("from") from: Int = 0,
        @Query("size") size: Int = 40,
        @Query("search") query: String
    ): RecipeResult
}

object RecipeApi {
    val retrofitService : RecipeApiService by lazy {
        retrofit.create(RecipeApiService::class.java)
    }
}