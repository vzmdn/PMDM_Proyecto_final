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

class MainViewModel(val photoRepository: PhotoRepository) : ViewModel() {

    private val _photo = MutableLiveData<Photo>()
    val photo: LiveData<Photo> = _photo


    fun fetchPhoto(date:String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val photo = photoRepository.fetchPhoto(date)
                _photo.postValue(photo)
            } catch (e: Exception) {
                Log.i("Tests", "Error fetching photo: ${e.message.orEmpty()}")
            }
        }

    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as NasaApplication
                MainViewModel(application.photoRepository)
            }
        }
    }

}
