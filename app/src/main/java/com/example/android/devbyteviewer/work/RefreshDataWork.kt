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

package com.example.android.devbyteviewer.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.devbyteviewer.database.getDatabase
import com.example.android.devbyteviewer.repository.VideosRepository
import retrofit2.HttpException

/**
 * TODO 08 - WorkManger
 * You're going to upgrade DevByteViewer to pre-fetch data when the app is in the background.
 * You should use the WorkManager library to accomplish this.
 */

// CoroutineWorker: We're going to use a CoroutineWorker, because we want to use coroutines to
// handle our asynchronous code and threading.
class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {


    // The doWork() function now returns Result instead of Payload because they have combined Payload into Result.
    // Read more here - https://developer.android.com/jetpack/androidx/releases/work#1.0.0-alpha12
    override suspend fun doWork(): Result {

        // Database + Repository
        val database = getDatabase(applicationContext)
        val repository = VideosRepository(database)

        return try {
            repository.refreshVideos()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

}