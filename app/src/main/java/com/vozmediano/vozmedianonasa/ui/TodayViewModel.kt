package com.vozmediano.vozmedianonasa.ui

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

class TodayViewModel(val photoRepository: PhotoRepository) : ViewModel() {

    private val _loading = MutableLiveData(true)
    val loading = _loading

    private val _photo = MutableLiveData<Photo>()
    val photo: LiveData<Photo> = _photo

fun fetchPhoto() {
    _loading.postValue(true)
    viewModelScope.launch(Dispatchers.IO) {
            val photo = photoRepository.fetchPhoto()
            _photo.postValue(photo)
            _loading.postValue(false)
        }
}

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as NasaApplication
                TodayViewModel(application.photoRepository)
            }
        }
    }

}
