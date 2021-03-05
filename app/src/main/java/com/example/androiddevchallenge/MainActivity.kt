/*
 * Copyright 2021 The Android Open Source Project
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
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.MyTheme


class MainActivity : AppCompatActivity() {
    val countDownViewModel by viewModels<CountDownViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp(countDownViewModel)
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp(viewModel: CountDownViewModel = CountDownViewModel()) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Count Down Timer")
                }
            )
        }
    ) { innerPadding ->
        BodyContent(viewModel, Modifier.padding(innerPadding))
    }
}

@Composable
fun BodyContent(
    viewModel: CountDownViewModel,
    modifier: Modifier = Modifier
) {
    Column {
        Row {
            EditButton(onClick = { viewModel.setStart(3) }, text = "3")
            EditButton(onClick = { viewModel.setStart(5) }, text = "5")
            EditButton(onClick = { viewModel.setStart(10) }, text = "10")
        }
        Row {
            EditButton(
                onClick = { viewModel.countDown() },
                text = "Start"
            )
        }
        Spacer(Modifier.height(8.dp))
        CountDownRow(viewModel)
    }

}

@Composable
fun FinishAnimation() {

}

@Composable
fun CountDownRow(
    viewModel: CountDownViewModel,
) {
    Row(
        modifier = Modifier
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "${viewModel.restTime}",
            style = MaterialTheme.typography.h1,
            modifier = Modifier.alpha(viewModel.alpha)
        )
    }
}

@Composable
fun EditButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    TextButton(
        onClick = onClick,
        shape = CircleShape,
        enabled = enabled,
        modifier = modifier
    ) {
        Text(text)
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
