package com.shishkin.luxuriouswatchface.util

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StabilizingWorker<T : Any>(
    private val scope: CoroutineScope,
    private val work: (T) -> Unit,
    private val stabilizingTime: Long = 500
) {

    private val data = MutableStateFlow<T?>(null)

    init {
        scope.launch {
            data.collectLatest { v ->
                if(v == null) return@collectLatest
                Log.e("customData", "StabilizingWorker start work v - $v")
                delay(stabilizingTime)
                work(v)
            }
        }
    }

    fun setData(newData: T?){
        data.value = newData
    }
}