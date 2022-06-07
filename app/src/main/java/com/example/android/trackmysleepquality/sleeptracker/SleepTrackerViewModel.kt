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
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.AndroidViewModel
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {

                private var viewModelJob = Job()

        override fun onCleared() {
                super.onCleared()
                viewModelJob.cancel()
        }

        private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

        private var tonight = MutableLiveData<SleepNight?>()

        private val nights = database.getAllNights()

        init {
                initializeTonight() {
                }

                private fun initializeTonight() {
                        uiScope.launch {
                                tonight.value = getTonightFromDatabase()
                        }
                }

        private suspend fun getTonightFromDatabase(): SleepNight? {
                return withContext(Dispatchers.IO) {
                        var night = database.getTonight()
                        if (night?.endTimeMilli != night?.startTimeMilli) {
                                night = null
                        }
                        night
                }
        }

                fun onStartTracking() {
                        uiScope.launch {

                                val newNight = SleepNight()

                                insert(newNight)

                                tonight.value = getTonightFromDatabase()
                        }
                }
private suspend fun insert(night: SleepNight){
        withContext(Dispatchers.IO) {
                database.insert(night)
        }
}

                fun onStopTracking() {
                        uiScope.launch {

                                val oldNight = tonight.value ?: return@launch

                                oldNight.endTimeMilli = System.currentTimeMillis()

                               update(oldNight)
                        }
                }

      private suspend fun update(night: SleepNight) {
              withContext(Dispatchers.IO) {
                      database.update(night)
              }
      }
                fun onClear() {
                        uiScope.launch {
                                clear()
                                tonight.value = null
                        }
                }


                suspend fun clear() {
                        withContext(Dispatchers.IO) {
                                database.clear()
                        }
                }




 fun someWorkNeedsToBeDone {
         uiScope.launch {

                 suspendFunction()
         }
 }

 suspend fun suspendFunction() {
         withContext(Dispatchers.IO) {
                 longrunningWork()
         }
 }

        }



    //TODO (01) Declare Job() and cancel jobs in onCleared().

    //TODO (02) Define uiScope for coroutines.

    //TODO (03) Create a MutableLiveData variable tonight for one SleepNight.

    //TODO (04) Define a variable, nights. Then getAllNights() from the database
    //and assign to the nights variable.

    //TODO (05) In an init block, initializeTonight(), and implement it to launch a coroutine
    //to getTonightFromDatabase().

    //TODO (06) Implement getTonightFromDatabase()as a suspend function.

    //TODO (07) Implement the click handler for the Start button, onStartTracking(), using
    //coroutines. Define the suspend function insert(), to insert a new night into the database.

    //TODO (08) Create onStopTracking() for the Stop button with an update() suspend function.

    //TODO (09) For the Clear button, created onClear() with a clear() suspend function.

    //TODO (12) Transform nights into a nightsString using formatNights().

}

