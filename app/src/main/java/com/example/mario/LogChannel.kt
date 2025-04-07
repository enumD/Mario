package com.example.mario

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object LogChannel
{
    private val _logFlow = MutableSharedFlow<String>()
    
    val logFlow = _logFlow.asSharedFlow() // Read-only access

    suspend fun sendLog(message: String) 
    {
        _logFlow.emit(message) // Emit a new log message
    }
}