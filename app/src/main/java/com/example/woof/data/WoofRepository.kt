/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.woof.data


import com.example.woof.model.Dog
import com.example.woof.network.WoofApiService

/**
 * Repositorio que obtiene la información de los perros
 * con una llamada al API a través de WoofApiService
 *
 */
interface WoofRepository {
    /** Obtiene las fotos de los perros */
    suspend fun getDogPhotos(): List<Dog>
}

/**
 * Network Implementation de repositorio que obtiene las fotos de los perros
 */
class NetworkDogsRepository(
    private val woofApiService: WoofApiService
) : WoofRepository {
    /** Obtiene las fotos de los perros */
    override suspend fun getDogPhotos(): List<Dog> = woofApiService.getWoofs()
}
