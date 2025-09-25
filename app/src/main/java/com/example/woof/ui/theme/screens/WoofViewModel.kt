package com.example.woof.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.woof.WoofApplication
import com.example.woof.data.WoofRepository
import com.example.woof.model.Dog
import com.example.woof.model.DogWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by Your name on 02/02/2024.
 */
class WoofViewModel(private val woofRepository: WoofRepository) : ViewModel(){

    val WOOF_TAG:String  = "woofApp";

    //Contiene la lista de dogs. Si se modifica alguno, se hara recompose
    private val _uiState = MutableStateFlow<WoofUiState>(WoofUiState.Loading)
    val uiState: StateFlow<WoofUiState> = _uiState.asStateFlow()

    private fun getDogWrappers(dogs: List<Dog>): List<DogWrapper> {
        val dogWrappers = dogs.mapIndexed { index, dog ->
            DogWrapper(dog = dog, id = index, expanded = false)
        }
        return dogWrappers
    }


    init {
        getDogs()
    }

    /**
     * Lanza la corrutina para obtener del repositorio los perros
     */
    fun getDogs() {
        Log.d(WOOF_TAG, "getDogs");

        viewModelScope.launch {
            _uiState.value = WoofUiState.Loading
            try {
                Log.d(WOOF_TAG, "getDogs Launch");
                val dogWrappers = getDogWrappers(woofRepository.getDogPhotos())
                Log.w(WOOF_TAG, dogWrappers.size.toString());

                _uiState.value = WoofUiState.Success(dogWrappers)
            } catch (e: IOException) {
                Log.e(WOOF_TAG, e.toString())
                _uiState.value = WoofUiState.Error
            } catch (e: HttpException) {
                Log.e(WOOF_TAG, e.toString())
                _uiState.value = WoofUiState.Error
            }
        }
    }

    /**
     * Obtiene de AndroidManifest.xml dónde hacemos la inyección de dependencias: WoofApplication -> DefaultAppContainer
     *
     * En ese flujo se crea:
     *
     * 1.- Service
     * 2.- Se inyecta en el repositorio
     *
     * ASí disponemos del repositorio para poder inyectarselo en este punto al viewModel
     *
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WoofApplication)
                val woofRepository = application.container.woofRepository
                WoofViewModel(woofRepository = woofRepository)
            }
        }
    }

    /**
     * Busca el dogId dentro de dogWrapper le cambia su valor expanded al contrario del que tenia
     * Al cambiar su valor habrá recompose en la UI.
     */
    fun onDetailSelected(dogId: Int) {
        _uiState.update { currentState ->
            when (currentState) {
                is WoofUiState.Success -> {
                    val updatedDogWrappers = currentState.dogs.map { dogWrapper ->
                        if (dogWrapper.id == dogId) {
                            dogWrapper.copy(expanded = !dogWrapper.expanded)
                        } else {
                            dogWrapper
                        }
                    }
                    currentState.copy(dogs = updatedDogWrappers)
                }
                else -> currentState // No changes for Error or Loading states
            }
        }
    }
}