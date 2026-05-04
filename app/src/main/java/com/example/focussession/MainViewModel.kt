package com.example.focussession

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _timer = MutableStateFlow(0)
    val timer = _timer.asStateFlow()




    fun startTimer(duration: Int) {
        _timer.value = duration
        updateTimer()
    }

    fun updateTimer() {
        viewModelScope.launch {
            while (_timer.value > 0) {
                delay(1000L)
                _timer.value -= 1
            }
        }
    }

    fun resetTimer() {
        _timer.value = 0
    }


}