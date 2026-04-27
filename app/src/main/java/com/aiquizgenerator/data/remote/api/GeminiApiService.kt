package com.aiquizgenerator.data.remote.api
import com.aiquizgenerator.data.remote.model.GeminiRequest
import com.aiquizgenerator.data.remote.model.GeminiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApiService {
    @POST("v1/models/gemini-2.5-flash:generateContent")
    suspend fun generateContent(@Query("key") apiKey: String, @Body request: GeminiRequest): Response<GeminiResponse>
}
