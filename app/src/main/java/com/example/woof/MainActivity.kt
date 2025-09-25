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

package com.example.woof

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material3.Icon
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.woof.ui.theme.WoofTheme
import com.example.woof.vm.WoofViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.woof.data.Dog
import com.example.woof.data.DogWrapper

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<WoofViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WoofTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    viewModel.loadDogWrappers()
                    WoofApp(viewModel)
                }
            }
        }
    }
}

/**
 * Obtenemos el viewModel y el uiState, enviamos esta informacion hacia los metodos inferiores.
 * No enviamos directamente el viewModel, para así no tener dependencia de este metodo y poder hacer renderizado
 * de las previews
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WoofApp(viewModel: WoofViewModel = viewModel()) {
    val woofState by viewModel.uiState.collectAsState()
    DogsGrid(dogsWrapper = woofState.dogWrapper, onClick ={viewModel.onDetailSelected(it)})
}


/**
 * Muestra la app bar y una lista de perros
 *
 * Al no depender de viewModel, podemos ver su vista previa
 *
 */
@Composable
fun DogsGrid(dogsWrapper: List<DogWrapper>,onClick: (Int) -> Unit) {
    Scaffold(
        topBar = {
            WoofTopAppBar()
        }
    ) { it ->
        LazyColumn(contentPadding = it) {
            items(dogsWrapper) {
                DogItem(
                    dogWrapper = it,
                    onClick = onClick,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
                )
            }
        }
    }
}

/**
 * Composable que muestra una lista que contiene el icono del perror y su informacion
 *
 * @param dogWrapper contiene la informacion relevante del perro
 * @param onClick evento que se ejecuta al hacer click sobre un perro
 * @param modifier
 */
@Composable
fun DogItem(
    dogWrapper: DogWrapper,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(   modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_small))
            ) {
                DogIcon(dogWrapper.dog.imageResourceId)
                DogInformation(dogWrapper.dog.name, dogWrapper.dog.age)
                Spacer(modifier = Modifier.weight(1f))
                DogItemButton(
                    expanded = dogWrapper.expanded,
                    onClick = { onClick(dogWrapper.id) }
                )
            }
            if (dogWrapper.expanded) {
                DogHobby(
                    dogWrapper.dog.hobbies,
                    modifier = Modifier.padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        top = dimensionResource(R.dimen.padding_small),
                        end = dimensionResource(R.dimen.padding_medium),
                        bottom = dimensionResource(R.dimen.padding_medium)
                    )
                )
            }
        }
    }
}


@Composable
private fun DogItemButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = stringResource(R.string.expand_button_content_description),
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

/**
 * Muestra el hobby del perror cuando se haga click sobre el
 */
@Composable
fun DogHobby(
    @StringRes dogHobby: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.about),
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = stringResource(dogHobby),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * Muestra la imagen del perro
 *
 * @param dogIcon es el ID de la imagen del perro
 * @param modifier
 */
@Composable
fun DogIcon(
    @DrawableRes dogIcon: Int,
    modifier: Modifier = Modifier
)  {
    Image(
        modifier = modifier
            .size(dimensionResource(R.dimen.image_size))
            .padding(dimensionResource(R.dimen.padding_small))
            .clip(MaterialTheme.shapes.small),
        contentScale = ContentScale.Crop,
        painter = painterResource(dogIcon),
        contentDescription = null
    )
}

/**
 * Composable that displays a dog's name and age.
 *
 * @param dogName is the resource ID for the string of the dog's name
 * @param dogAge is the Int that represents the dog's age
 * @param modifier modifiers to set to this composable
 */
@Composable
fun DogInformation(
    @StringRes dogName: Int,
    dogAge: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(dogName),
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small)),
            style = MaterialTheme.typography.displayMedium,
        )
        Text(
            text = stringResource(R.string.years_old, dogAge),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * Barra horizontal que contiene el logo Woof
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoofTopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Row(   verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.image_size))
                        .padding(dimensionResource(id = R.dimen.padding_small)),
                    painter = painterResource(R.drawable.ic_woof_logo),
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displayLarge
                )
            }
        },
        modifier = modifier
    )
}



@Preview
@Composable
fun DogsGridPreview() {

    val dog1 = Dog(R.drawable.koda, R.string.dog_name_1, 2, R.string.dog_description_1)
    val dogWrapper1 = DogWrapper(dog = dog1, expanded = false, id = 1)
    val dog2 = Dog(R.drawable.bella, R.string.dog_name_1, 8, R.string.dog_description_2)
    val dogWrapper2 = DogWrapper(dog = dog2, expanded = false, id = 2)
    val dog3 = Dog(R.drawable.faye, R.string.dog_name_1, 12, R.string.dog_description_3)
    val dogWrapper3 = DogWrapper(dog = dog3, expanded = false, id = 3)

    val dogWrapperList = listOf(dogWrapper1, dogWrapper2, dogWrapper3)
    DogsGrid(
        dogsWrapper = dogWrapperList,
        onClick = { }
    )

}

@Preview
@Composable
fun DogItemPreview() {

    val dog1 = Dog(R.drawable.koda, R.string.dog_name_1, 2, R.string.dog_description_1)
    val dogWrapper1 = DogWrapper(dog = dog1, expanded = false, id = 1)
    DogItem(
        dogWrapper = dogWrapper1,
        onClick = { }
    )

}


