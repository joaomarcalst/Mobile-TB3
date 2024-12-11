package com.example.automacorp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automacorp.service.ApiServices
import com.example.automacorp.model.RoomDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoomViewModel : ViewModel() {

    private val _roomsState = MutableStateFlow<List<RoomDto>>(emptyList())
    val roomsState: StateFlow<List<RoomDto>> get() = _roomsState

    private val _roomState = MutableStateFlow<RoomDto?>(null)
    val roomState: StateFlow<RoomDto?> get() = _roomState

    fun findAll() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ApiServices.roomsApiService.findAll().execute() }
                .onSuccess { response ->
                    _roomsState.value = response.body() ?: emptyList()
                }
                .onFailure {
                    it.printStackTrace()
                    _roomsState.value = emptyList()
                }
        }
    }

    fun findRoom(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ApiServices.roomsApiService.findById(id).execute() }
                .onSuccess { response ->
                    _roomState.value = response.body()
                }
                .onFailure {
                    it.printStackTrace()
                    _roomState.value = null
                }
        }
    }

    fun updateRoom(id: Long, roomDto: RoomDto) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ApiServices.roomsApiService.updateRoom(id, roomDto).execute() }
                .onSuccess { response ->
                    _roomState.value = response.body()
                }
                .onFailure {
                    it.printStackTrace()
                }
        }
    }
}