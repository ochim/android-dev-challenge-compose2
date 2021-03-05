package com.example.androiddevchallenge

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import android.os.CountDownTimer


class CountDownViewModel : ViewModel() {
    private var startTime = 3

    var restTime by mutableStateOf(startTime)
        private set

    var alpha by mutableStateOf(0.5f)
        private set

    fun setStart(num: Int) {
        startTime = num
        restTime = num
    }

    fun countDown(finish: () -> Unit = {}) {
        object : CountDownTimer(1000L * startTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restTime--
            }

            override fun onFinish() {
                finish()
                alpha = 0.5f
            }
        }.start()
        alpha = 1.0f
    }

}