package com.canonal.flows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.math.sqrt

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
            countDownFlow
                //filter returns a flow
                .filter { currentValue ->
                    //if expression is true we continue and collect it
                    //else we throw it away
                    //take only even numbers
                    currentValue % 2 == 0
                }
                //map filtered value to new value
                //(you can change the order- first map then filter)
                .map { currentValue ->
                    //take the square of currentValue and map it to currentValue
                    currentValue * currentValue
                }
                //you can do something on each value
                .onEach { currentValue ->
                    val root = sqrt(currentValue.toDouble())
                    println("Current time onEach: $root")
                //it can also be used as below outside of viewModelScope
//                    countDownFlow.onEach {
//                        print(it)
//                    }.launchIn(viewModelScope)
                }
                .collect { currentValue ->
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