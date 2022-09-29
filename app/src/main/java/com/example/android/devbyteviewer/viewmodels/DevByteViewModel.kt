/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.devbyteviewer.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android.devbyteviewer.database.getDatabase
import com.example.android.devbyteviewer.repository.VideosRepository
import kotlinx.coroutines.launch

/**
 * DevByteViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 *
 * @param application The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during actiivty
 * or fragment lifecycle events.
 */
class DevByteViewModel(application: Application) : AndroidViewModel(application) {

    // Create the database singleton.
    private val database = getDatabase(application)

    // Then create your repository using the database singleton
    private val videosRepository = VideosRepository(database)

    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        viewModelScope.launch {

            // Create an init block and launch a coroutine to call videosRepository.refreshVideos().
            videosRepository.refreshVideos()

        }

        // refreshDataFromNetwork()
    }

    // Get videos LiveData from the repository and assign it to a playlist variable.
    val playlist = videosRepository.videos


    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DevByteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DevByteViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

/**
 *
 */

/**
 *
 */

/**
 * A playlist of videos that can be shown on the screen. This is private to avoid exposing a
 * way to set this value to observers.
 */
// private val _playlist = MutableLiveData<List<Video>>()

/**
 * A playlist of videos that can be shown on the screen. Views should use this to get access
 * to the data.
 */
// val playlist: LiveData<List<Video>>
//     get() = _playlist

/**
 * Refresh data from network and pass it via LiveData. Use a coroutine launch to get to
 * background thread.
 */
// private fun refreshDataFromNetwork() = viewModelScope.launch {
//     try {
//        val playlist = Network.devbytes.getPlaylist().await()
//        _playlist.postValue(playlist.asDomainModel())
//    } catch (networkError: IOException) {
// Show an infinite loading spinner if the request fails
// challenge exercise: show an error to the user if the network request fails
//    }
//}
