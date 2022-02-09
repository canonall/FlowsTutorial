package com.canonal.flows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
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

    val countDownFlow2 = flow<Int> {
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
        collectCountDownFlow2()
    }

    private fun collectCountDownFlow2() {
        viewModelScope.launch {
            val reduceResult = countDownFlow2
                //executed for first two emission
                //combines its result at accumulator
                //and adds 3rd emission and so on
                //(10x11)/2 = 55
                .reduce { accumulator, value ->
                    accumulator + value
                }
                //does the same thing as reduce but with an initial value
                //100 + (10x11)/2 = 155
//                .fold(100){acc, value ->
//                    acc + value
//                }

            println("Reduce result is $reduceResult")
        }
    }

    //instead of UI, you can also observe it in ViewModel
    private fun collectCountDownFlow() {
        viewModelScope.launch {
            val count = countDownFlow
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
//                .collect { currentValue ->
//                    println("Current time $currentValue")
//                }
                //counts the value that matches a specific condition
                //and returns the number of values in this flow
                .count { currentValue ->
                    currentValue % 2 == 0
                }
            println("Count is $count")
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