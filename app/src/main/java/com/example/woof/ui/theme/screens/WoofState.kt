package com.example.woof.ui.screens

import com.example.woof.model.DogWrapper

/**
 * Created by Your name on 31/01/2024.
 */
sealed interface WoofUiState {
    data class Success(val dogs: List<DogWrapper>) : WoofUiState
    object Error : WoofUiState
    object Loading : WoofUiState
}