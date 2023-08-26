package com.example.wallpaperapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.wallpaperapp.models.PhotoResponse
import com.example.wallpaperapp.paging.WallpaperPagingSource
import com.example.wallpaperapp.retrofit.WallpaperApi
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WallpaperRepository @Inject constructor(private val wallpaperApi: WallpaperApi) {

    fun getWallpaper(): Flow<PagingData<PhotoResponse>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = 15,
                pageSize = 15,
                maxSize = 100,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { WallpaperPagingSource(wallpaperApi) }
        ).flow
    }
}
