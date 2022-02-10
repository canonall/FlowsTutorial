package com.canonal.flows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
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

    private val _stateFlow = MutableStateFlow(0)
    val stateFlow: StateFlow<Int>
        get() = _stateFlow

    init {
        collectCountDownFlow()
    }

    fun incrementCounter() {
        _stateFlow.value += 1
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
                .collectLatest { food ->
                    println("FLOW: Now eating $food")
                    delay(1500L)
                    println("FLOW: Finished eating $food")
                }
        }
    }

}