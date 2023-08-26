package com.example.wallpaperapp

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallpaperapp.paging.WallpaperPagingAdapter
import com.example.wallpaperapp.utils.Resource
import com.example.wallpaperapp.viewmodels.WallpaperViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
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

        if(isConnectedToInternet()) {
            lifecycleScope.launch {
                wallpaperViewModel.getAllPhotos.collectLatest { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val pagingData = resource.data
                            if (pagingData != null) {
                                adapter.submitData(pagingData)
                            }
                        }

                        is Resource.Loading -> {
                            Toast.makeText(this@MainActivity, "Loading", Toast.LENGTH_LONG).show()
                        }

                        is Resource.Error -> {
                            Toast.makeText(
                                this@MainActivity,
                                "Can't load the data",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }
                }

            }
        }
        else{
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show()
        }
    }
    private fun isConnectedToInternet(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
