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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {
    private val countDownViewModel by viewModels<CountDownViewModel>()

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
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        CountItemEntryInput(viewModel)
        Spacer(Modifier.height(16.dp))
        Row {
            EditButton(
                onClick = { viewModel.setStart(3) },
                text = "3 sec",
                enabled = !viewModel.isPlaying
            )
            EditButton(
                onClick = { viewModel.setStart(5) },
                text = "5 sec",
                enabled = !viewModel.isPlaying
            )
            EditButton(
                onClick = { viewModel.setStart(10) },
                text = "10 sec",
                enabled = !viewModel.isPlaying
            )
        }
        Spacer(Modifier.height(16.dp))
        Row(modifier = Modifier.padding(start = 16.dp)) {
            EditButton(
                onClick = {
                    viewModel.countDown()
                },
                text = "Start",
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary
                ),
                enabled = !viewModel.isPlaying
            )
            Spacer(Modifier.width(16.dp))
            EditButton(
                onClick = {
                    viewModel.pause()
                },
                text = "Pause",
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary
                ),
                enabled = viewModel.isPlaying
            )
        }
        Spacer(Modifier.height(16.dp))

        // count down display
        val text = if (viewModel.restTime == 0) "Lift off!" else "${viewModel.restTime}"
        Text(
            text = text,
            style = MaterialTheme.typography.h1,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
                .alpha(viewModel.alpha)
        )
    }
}

// @Composable
// fun FinishAnimation() {
// }

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CountItemEntryInput(viewModel: CountDownViewModel) {
    val (text, onTextChange) = rememberSaveable { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val submit = {
        if (text.isNotBlank()) {
            try {
                val i = text.toInt()
                viewModel.setStart(i)
                onTextChange("")
                keyboardController?.hideSoftwareKeyboard()
            } catch (e: Exception) {
            }
        }
    }

    CountItemInput(
        text = text,
        enabled = !viewModel.isPlaying,
        onTextChange = onTextChange,
        submit = submit,
    ) {
        EditButton(
            onClick = submit,
            text = "SET",
            enabled = text.isNotBlank() && !viewModel.isPlaying
        )
    }
}

@Composable
fun CountItemInput(
    text: String,
    enabled: Boolean = true,
    onTextChange: (String) -> Unit,
    submit: () -> Unit,
    buttonSlot: @Composable () -> Unit,
) {
    Column {
        Row(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .height(IntrinsicSize.Min)
        ) {
            InputText(
                text = text,
                onTextChange = onTextChange,
                onImeAction = submit,
                modifier = Modifier.weight(1f),
                enabled = enabled,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(Modifier.align(Alignment.CenterVertically)) { buttonSlot() }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputText(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onImeAction: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = text,
        onValueChange = onTextChange,
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
                keyboardController?.hideSoftwareKeyboard()
            }
        ),
        modifier = modifier,
        enabled = enabled,
    )
}

@Composable
fun EditButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    enabled: Boolean = true
) {
    TextButton(
        onClick = onClick,
        shape = CircleShape,
        enabled = enabled,
        modifier = modifier,
        colors = colors,
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
