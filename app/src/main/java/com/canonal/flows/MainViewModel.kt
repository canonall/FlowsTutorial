package com.canonal.flows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapConcat
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

    private fun collectCountDownFlow() {
        val flow1 = flow {
            emit(1)
            delay(1000L)
            emit(5)
        }
        viewModelScope.launch {
            //flatMapConcat returns flow
            //first emit of flow1 comes and its value is used in second flow
            //flatMapMerge and flatMapLatest also exist
            flow1.flatMapConcat { value ->
                flow {
                    emit(value + 10)
                    delay(1000L)
                    emit(value + 11)
                }
            }.collect { value ->
                println("The value is $value")
            }
        }
    }

}