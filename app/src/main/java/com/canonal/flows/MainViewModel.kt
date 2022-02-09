package com.canonal.flows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val countDownFlow = flow<Int> {
        val startValue = 10
        var currentValue = startValue
        emit(startValue)
        while (currentValue > 0) {
            delay(1000L)
            currentValue--
            emit(currentValue)
        }
    }

    init {
        collectCountDownFlow()
    }

    //instead of UI, you can also observe it in ViewModel
    private fun collectCountDownFlow() {
        viewModelScope.launch {
            countDownFlow.collect { currentValue ->
                println("Current time $currentValue")
            }
        }
//        viewModelScope.launch {
//            countDownFlow.collectLatest { currentValue ->
//                delay(1500L)
//                //Except the last value others will be cancelled because after
//                //1000ms a new emit comes.
//                println("Current time $currentValue")
//            }
//        }
    }

}