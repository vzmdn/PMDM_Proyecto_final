package com.vozmediano.vozmedianonasa.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vozmediano.vozmedianonasa.NasaApplication
import com.vozmediano.vozmedianonasa.domain.PhotoRepository
import com.vozmediano.vozmedianonasa.domain.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThisWeekViewModel(val photoRepository: PhotoRepository) : ViewModel() {

    private val _photos = MutableStateFlow<List<Photo?>>(emptyList())
    val photos: StateFlow<List<Photo?>> = _photos.asStateFlow()


    fun fetchPhotos(startDate: String, endDate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val photoList = photoRepository.fetchPhotos(startDate, endDate)
                _photos.value = photoList
            } catch (e: Exception) {
                Log.i("Tests", "${e.printStackTrace()}")
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as NasaApplication
                ThisWeekViewModel(application.photoRepository)
            }
        }
    }
}