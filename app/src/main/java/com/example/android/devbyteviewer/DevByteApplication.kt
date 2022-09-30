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

package com.example.android.devbyteviewer

import android.app.Application
import android.os.Build
import androidx.work.*
import com.example.android.devbyteviewer.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Override application to setup background work via WorkManager
 */
class DevByteApplication : Application() {

    //
    val applicationScope = CoroutineScope(Dispatchers.Default)

    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        // We want to run this function in the background, before the first screen will be shown
        delayedInit()
    }

    /**
     * We want to schedule our RefreshDataWork to run once a day
     */

    // We are using Coroutine to launch the process
    private fun delayedInit() = applicationScope.launch {

        // We are asking to run our job
        setupRecurringWork()

    }

    private fun setupRecurringWork() {

        /**
         *
         */
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()


        /**
         * Scheduled work to run every day
         * Define "conditions" under which the WorkManager will be executed:
         */
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        // Run regularly / on periodic basis
        WorkManager.getInstance().enqueueUniquePeriodicWork(

            // Unique name for this scheduled work: RefreshDataWorker
            // We have stored this unique name in the work/RefreshDataWork.kt file
            RefreshDataWorker.WORK_NAME, // RefreshDataWorker

            // What to do when two requests of the same unique work are enqueued.
            // We will keep the previous periodic work, which will discard the new work request.
            ExistingPeriodicWorkPolicy.KEEP,

            //
            repeatingRequest
        )

    }


}
