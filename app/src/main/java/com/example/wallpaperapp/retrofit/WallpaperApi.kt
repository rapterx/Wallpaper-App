package com.example.wallpaperapp.retrofit

import com.example.wallpaperapp.models.PhotoResponse
import com.example.wallpaperapp.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WallpaperApi {

    @Headers("Accept-Version: v1", "Authorization: Client-ID $API_KEY")
    @GET("photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<List<PhotoResponse>>
}