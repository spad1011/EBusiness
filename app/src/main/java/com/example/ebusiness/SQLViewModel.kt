package com.example.ebusiness

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ebusiness.entities.Event
import com.example.ebusiness.entities.EventLite
import com.example.ebusiness.repository.TicketEventDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SQLViewModel(application: Application) : AndroidViewModel(application) {

    private val ticketEventDao = TicketEventDatabase.getDatabase(application).ticketEventDao()

    private val _events = MutableStateFlow<List<EventLite>>(emptyList())
    val events = _events.asStateFlow()

    private val _event = MutableStateFlow<Event?>(null)
    val event = _event.asStateFlow()

    private val _insertionResult = MutableStateFlow<InsertState>(InsertState.Idle)
    val insertionResult = _insertionResult.asStateFlow()

    private val _deleteResult = MutableStateFlow<Boolean>(false)
    val deleteResult = _deleteResult.asStateFlow()

    fun getAllLite() {
        viewModelScope.launch {
            _events.value = ticketEventDao.getAllLite();
        }
    }

    fun getAllLiteFilterByName(name: String?) {
        Log.d("SQLViewModel", "Getting all lite events, input: $name")
        viewModelScope.launch {
            _events.value = ticketEventDao.getAllLiteFilterByName(name)
        }
    }

    fun getEventById(id: Int) {
        viewModelScope.launch {
            _event.value = ticketEventDao.getEventById(id)
        }
    }

    fun insertEvent(event: Event) {
        Log.d("SQLViewModel", "Inserting event: $event")
        viewModelScope.launch {
            try {
                _insertionResult.value = InsertState.Loading
                val result = ticketEventDao.insert(event)
                if (result > 0) {
                    _insertionResult.value = InsertState.Success
                } else {
                    _insertionResult.value = InsertState.Failure("Failed to insert event")
                }
            } catch (e: Exception) {
                _insertionResult.value = InsertState.Failure("Error inserting event: ${e.message}")
            }
        }
    }

    fun deleteEventById(id: Int) {
        Log.d("SQLViewModel", "Deleting event with id: $id")
        viewModelScope.launch {
            val deletedRows = ticketEventDao.deleteEventById(id)
            _deleteResult.value = deletedRows > 0
        }
    }

    fun resetInsertState() {
        _insertionResult.value = InsertState.Idle
    }
}

sealed class InsertState {
    object Idle : InsertState()
    object Success : InsertState()
    object Loading : InsertState()
    data class Failure(val errorMessage: String) : InsertState()
}
