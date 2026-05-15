package com.example.grama_vaxihealthcare.network

import com.example.grama_vaxihealthcare.data.entity.CampAlert
import retrofit2.http.GET

interface ApiService {
    @GET("camps")
    suspend fun getUpcomingCamps(): List<CampAlert>
}
