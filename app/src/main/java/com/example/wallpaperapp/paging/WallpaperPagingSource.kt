package com.example.wallpaperapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.wallpaperapp.models.PhotoResponse
import com.example.wallpaperapp.retrofit.WallpaperApi
import com.example.wallpaperapp.utils.Constants.API_KEY

class WallpaperPagingSource(private val wallpaperApi: WallpaperApi) : PagingSource<Int, PhotoResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoResponse> {
        return try {
            val position = params.key ?: 1
            val response = wallpaperApi.getPhotos(position,params.loadSize)

            LoadResult.Page(
                data = response,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (response.isEmpty()) null else position + 1,
            )
        } catch (e: Exception) {
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