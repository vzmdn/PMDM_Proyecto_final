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

class PickDateViewModel(val photoRepository: PhotoRepository): ViewModel() {
    private val _loading = MutableLiveData(true)
    val loading = _loading

    private val _photo = MutableLiveData<Photo>()
    val photo: LiveData<Photo> = _photo

    fun fetchPhoto(date: String) {
        _loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
                try {
                    val photo = photoRepository.fetchPhoto(date)
                _photo.postValue(photo)
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
                PickDateViewModel(application.photoRepository)
            }
        }
    }

}
