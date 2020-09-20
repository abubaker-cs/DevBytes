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

package com.example.android.devbyteviewer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.devbyteviewer.database.VideosDatabase
import com.example.android.devbyteviewer.database.asDomainModel
import com.example.android.devbyteviewer.domain.Video
import com.example.android.devbyteviewer.network.Network
import com.example.android.devbyteviewer.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * TODO 07 - Repository
 *
 * A Repository is just a regular class that has one (or more) methods that load data without
 * specifying the data source as part of the main API
 *
 * 1. Because it's just a regular class, there's no need for an annotation to define a repository.
 * 2. The repository hides the complexity of managing the interactions between the database and the
 *    networking code .
 */
class VideosRepository(private val database: VideosDatabase) {

    val videos: LiveData<List<Video>> = Transformations.map(database.videoDao.getVideos()) {
        it.asDomainModel()
    }

    // It will be used to refresh the offline cache, while benefiting from coroutines
    suspend fun refreshVideos() {

        // Run your code on the IO Dispatcher
        withContext(Dispatchers.IO) {

            // 1. Make a network call to getPlaylist() and by using await() - tells the coroutine
            //    to suspend until the data is available.
            val playlist = Network.devbytes.getPlaylist().await()

            // 2. insertAll() - insert the playlist into the database
            database.videoDao.insertAll(*playlist.asDatabaseModel())

            // *playlist - Note the asterisk * is the spread operator. It allows you to pass in an
            // array to a function that expects varargs.
        }
    }
}
