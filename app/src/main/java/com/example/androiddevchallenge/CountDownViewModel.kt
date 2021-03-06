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

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CountDownViewModel : ViewModel() {
    private val defaultAlpha = 0.6f

    var restTime by mutableStateOf(3)
        private set

    var alpha by mutableStateOf(defaultAlpha)
        private set

    var isPlaying by mutableStateOf(false)
        private set

    private var timer: CountDownTimer? = null

    fun setStart(num: Int) {
        restTime = num
        alpha = defaultAlpha
    }

    fun countDown(finish: () -> Unit = {}) {
        if (restTime < 1) return

        timer = object : CountDownTimer(1000L * restTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restTime--
            }

            override fun onFinish() {
                isPlaying = false
                finish()
            }
        }.start()
        alpha = 1.0f
        isPlaying = true
    }

    fun pause() {
        timer?.cancel()
        isPlaying = false
        alpha = defaultAlpha
    }
}
