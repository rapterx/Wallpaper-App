package com.example.wallpaperapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallpaperapp.paging.WallpaperPagingAdapter
import com.example.wallpaperapp.viewmodels.WallpaperViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var wallpaperViewModel: WallpaperViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WallpaperPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)

        wallpaperViewModel = ViewModelProvider(this)[WallpaperViewModel::class.java]

        adapter = WallpaperPagingAdapter()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            wallpaperViewModel.getAllPhotos.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

    }
}