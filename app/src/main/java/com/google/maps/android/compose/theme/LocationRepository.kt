package com.google.maps.android.compose.theme

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationRepository @Inject constructor (private val locationDao: LocationDao) {

    val scope = CoroutineScope(Dispatchers.IO)


    val allLocations: LiveData<List<LocationEntity>> = MutableLiveData<List<LocationEntity>>().apply {
        scope.launch {
            postValue(locationDao.getAll())
        }
    }

    @WorkerThread
    suspend fun insert(location: LocationEntity) {
        locationDao.insertAll(location)
    }
        //burada
    @WorkerThread
    suspend fun delete(location: Int?) {
        if (location != null) {
            locationDao.delete(location)
        }
    }
    @WorkerThread
    fun getLocationsByDurum(durum: Boolean): LiveData<List<LocationEntity>> {
        return locationDao.getLocationsByDurum(durum)
    }
}
