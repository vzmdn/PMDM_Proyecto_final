package com.vozmediano.vozmedianonasa.ui.today

import android.util.Log
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
import java.time.LocalDate

class TodayViewModel(val photoRepository: PhotoRepository) : ViewModel() {

    private val _photo = MutableStateFlow<Photo?>(null)
    val photo: StateFlow<Photo?> = _photo.asStateFlow()

    fun fetchPhoto() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val photo = photoRepository.fetchPhoto(LocalDate.now().toString())
                _photo.value = photo
            } catch (e: Exception) {
                Log.i("Tests", "${e.printStackTrace()}")
            }
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
