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
import kotlinx.coroutines.launch

class ThisWeekViewModel (val photoRepository: PhotoRepository): ViewModel() {

    private val _loading = MutableLiveData(true)
    val loading = _loading

    private val _photos = MutableLiveData<List<Photo>>()
    val photos : LiveData<List<Photo>> = _photos


    fun fetchPhotos(startDate:String,endDate:String) {
        _loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
                try {
                    val photoList = photoRepository.fetchPhotos(startDate, endDate)
                    _photos.postValue(photoList)
                } catch (e: Exception) {
                    Log.i("Tests", "${e.printStackTrace()}")
                }
                _loading.postValue(false)
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