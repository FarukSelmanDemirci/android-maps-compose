package com.google.maps.android.compose.theme

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//class LocationViewModel(application: Application) : AndroidViewModel(application)
@HiltViewModel
class LocationViewModel @Inject constructor (private val repository: LocationRepository) : ViewModel() {
    val allLocations: LiveData<List<LocationEntity>> = repository.allLocations

    fun insert(location: LocationEntity) = viewModelScope.launch {
        repository.insert(location)
    }

    fun delete(location: Int?) = viewModelScope.launch {
        repository.delete(location)
    }
    fun getLocationsByDurum(durum: Boolean): LiveData<List<LocationEntity>> {
        return repository.getLocationsByDurum(durum)
    }
}
