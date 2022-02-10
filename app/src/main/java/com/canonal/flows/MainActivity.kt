package com.canonal.flows

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.canonal.flows.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.btn.setOnClickListener {
           // mainViewModel.incrementCounter()
        }

        //you can use flow operators here as an advantage over livedata
//        collectLatestLifecycleFlow(mainViewModel.stateFlow) { value ->
//            binding.tvTimer.text = value.toString()
//        }


//        lifecycleScope.launch {
//            mainViewModel.countDownFlow.collectLatest { currentValue ->
//                binding.tvTimer.text = currentValue.toString()
//            }
//        }

    }

    //extension function for observing StateFlow as lifecycle aware
    //use fragment for fragment
    private fun <T> AppCompatActivity.collectLatestLifecycleFlow(
        flow: Flow<T>,
        collect: suspend (T) -> Unit
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(STARTED) {
                flow.collectLatest(collect)
            }
        }
    }
}