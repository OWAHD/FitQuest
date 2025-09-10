package com.example.fitquest.network



import com.example.fitquest.network.models.ApiResponse
import retrofit2.http.GET

interface FitQuestApiService {
    @GET("7ceb-eac4-4c33-ba92")
    suspend fun getExercises(): ApiResponse
}