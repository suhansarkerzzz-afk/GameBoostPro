package com.gameboost.pro

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameBoostViewModel(app: Application) : AndroidViewModel(app) {
    private val monitor = PerformanceMonitor(app)
    private val _stats = MutableStateFlow(DeviceStats())
    val stats: StateFlow<DeviceStats> = _stats
    private val _boost = MutableStateFlow(false)
    val boost: StateFlow<Boolean> = _boost
    init { viewModelScope.launch { while (true) { _stats.value = monitor.getStats(); delay(1000) } } }
    fun toggleBoost() { _boost.value = !_boost.value }
}
