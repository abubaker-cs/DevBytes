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

package com.example.android.devbyteviewer.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * TODO 04 - DAO for offline storage
 */

// Define an INSTANCE variable to store the singleton.
private lateinit var INSTANCE: VideosDatabase

@Dao
interface VideoDao {

    // 1. Get the videos from Cache, and return a LIST of DatabaseVideos using LiveData
    @Query("select * from databasevideo")
    fun getVideos(): LiveData<List<DatabaseVideo>>


    // 2. Store values in the cache, while REPLACING the last-saved value
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: DatabaseVideo)

}

/**
 * TODO 05 - Database Class
 *
 * Abstract VideosDatabase class that extends RoomDatabase, and annotate it with @Database, including:
 * 1. Entities and
 * 2. Version
 */

@Database(entities = [DatabaseVideo::class], version = 1)
abstract class VideosDatabase : RoomDatabase() {

    //  An abstract videoDao variable
    abstract val videoDao: VideoDao

    fun getDatabase(context: Context): VideosDatabase {
        return INSTANCE
    }

}

/**
 * TODO 06 - Return VideosDatabase
 */

fun getDatabase(context: Context): VideosDatabase {

    // Make sure your code is synchronized so it’s thread safe
    synchronized(VideosDatabase::class.java) {

        // Use ::INSTANCE.isInitialized to check if the variable has been initialized
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                VideosDatabase::class.java,
                "videos"
            ).build()
        }

    }

    return INSTANCE
}