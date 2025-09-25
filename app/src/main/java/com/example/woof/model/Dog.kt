/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.woof.model

import kotlinx.serialization.Serializable

/**
 * A data class to represent the information presented in the dog card
 */


@Serializable
data class Dog(
    val name: String,
    val age: Int,
    val url: String,
    val hobbies: String
)

/**
 * Clase que representa un dog y en mayores 2 atributos necesarios para la UI
 * Un id para poder buscar el dog sobre el cual hemos hecho click en la lista
 * Un expanded para saber si lo hemos expandido o no su detalle
 */
data class DogWrapper(
    val dog: Dog,
    val expanded: Boolean,
    val id: Int
)



