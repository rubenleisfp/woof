package com.example.woof.vm

import androidx.lifecycle.ViewModel
import com.example.woof.data.DogWrapper
import com.example.woof.data.dogs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Created by Your name on 02/02/2024.
 */
class WoofViewModel : ViewModel(){

    //Contiene la lista de dogs. Si se modifica alguno, se hara recompose
    private val _uiState = MutableStateFlow(WoofState(emptyList()))
    val uiState: StateFlow<WoofState> = _uiState.asStateFlow()


    /**
     * Carga la lista de dogWrapper, la información del Dog, más un id
     * y una variable expanded para indicar si el detalle está expandido o no
     */
    fun loadDogWrappers() {
        val dogWrappers = dogs.mapIndexed { index, dog ->
            DogWrapper(dog = dog, id = index, expanded = false)
        }
        _uiState.value.dogWrapper = dogWrappers
    }

    /**
     * Busca el dogId dentro de dogWrapper le cambia su valor expanded al contrario del que tenia
     * Al cambiar su valor habrá recompose en la UI.
     */
    fun onDetailSelected(dogId: Int) {

        _uiState.update { currentState ->
            val updatedDogWrappers = currentState.dogWrapper.map { dogWrapper ->
                if (dogWrapper.id == dogId) {
                    dogWrapper.copy(expanded = !dogWrapper.expanded)
                } else {
                    dogWrapper
                }
            }
            currentState.copy(dogWrapper = updatedDogWrappers)
        }
    }
}