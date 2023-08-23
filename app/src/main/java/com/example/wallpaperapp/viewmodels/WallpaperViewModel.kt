package com.example.wallpaperapp.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.wallpaperapp.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class WallpaperViewModel @Inject constructor(
    private val wallpaperRepository: WallpaperRepository,
    private val state: SavedStateHandle,
) : ViewModel() {



    private val photosChannel = Channel<String?>(Channel.CONFLATED)

    val getAllPhotos = flowOf(photosChannel.receiveAsFlow().map { PagingData.empty() },
        state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY).asFlow().flatMapLatest { id ->
            wallpaperRepository.getWallpaper()
        }.cachedIn(viewModelScope)
    ).flattenMerge()



    companion object {
        private const val CURRENT_QUERY = "current_query"
        private var DEFAULT_QUERY = "0"
    }
}