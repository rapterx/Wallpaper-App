package com.example.wallpaperapp.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.wallpaperapp.models.PhotoResponse
import com.example.wallpaperapp.repository.WallpaperRepository
import com.example.wallpaperapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class WallpaperViewModel @Inject constructor(
    private val wallpaperRepository: WallpaperRepository,
    private val state: SavedStateHandle,
) : ViewModel() {

    val getAllPhotos: Flow<Resource<PagingData<PhotoResponse>>> = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)
        .asFlow()
        .flatMapLatest { id ->
            wallpaperRepository.getWallpaper()
                .map { pagingData ->
                    if (pagingData== null) {
                        Resource.Error("No data available")
                    } else {
                        Resource.Success(pagingData)
                    }
                }
                .onStart { emit(Resource.Loading(null)) }
                .catch { e ->
                    emit(Resource.Error(e.message ?: "An error occurred"))
                }
                .flowOn(Dispatchers.IO)
        }

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private var DEFAULT_QUERY = "0"
    }
}
