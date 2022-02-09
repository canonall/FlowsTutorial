package com.canonal.flows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
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
        val flow = flow {
            delay(250L)
            emit("Appetizer")
            delay(1000L)
            emit("Main dish")
            delay(100L)
            emit("Dessert")
        }
        viewModelScope.launch {
            flow.onEach { food ->
                println("FLOW: $food is delivered")

            }
                //https://www.youtube.com/watch?v=sk3svS_fzZM
                //explains at 23.10, a bit complicated
                .conflate()
                .collect { food ->
                    println("FLOW: Now eating $food")
                    delay(1500L)
                    println("FLOW: Finished eating $food")
                }
        }
    }

}