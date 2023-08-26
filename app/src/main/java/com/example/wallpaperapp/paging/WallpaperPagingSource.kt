package com.example.wallpaperapp.paging

import android.util.Log
import android.widget.Toast
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.wallpaperapp.models.PhotoResponse
import com.example.wallpaperapp.retrofit.WallpaperApi
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import java.io.IOException


class WallpaperPagingSource(private val wallpaperApi: WallpaperApi) : PagingSource<Int, PhotoResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoResponse> {
        return try {
            val position = params.key ?: 1
            val response = wallpaperApi.getPhotos(position, params.loadSize)

            if (response.isSuccessful) {
                val photoResponseList = response.body()
                if (photoResponseList != null) {
                    LoadResult.Page(
                        data = photoResponseList,
                        prevKey = if (position == 1) null else position - 1,
                        nextKey = if (photoResponseList.isEmpty()) null else position + 1,
                    )
                } else {
                    LoadResult.Error(NullPointerException("Response body is null"))

                }
            } else {
                // Handle the case where the response is not successful
                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                LoadResult.Error(IOException("Unsuccessful response: ${response.code()}, $errorMsg"))
            }
        } catch (e: HttpException) {
            // Handle HTTP exceptions (e.g., 404, 500, etc.)
            LoadResult.Error(IOException("HTTP Error: ${e.message()}"))
        } catch (e: Exception) {
            // Handle other exceptions
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PhotoResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}