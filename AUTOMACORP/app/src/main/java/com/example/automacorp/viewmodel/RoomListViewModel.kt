package com.example.automacorp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automacorp.service.ApiServices
import com.example.automacorp.model.RoomDto
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class RoomListViewModel : ViewModel() {

    private val _rooms = MutableLiveData<List<RoomDto>>() // Backing property for room list
    val rooms: LiveData<List<RoomDto>> get() = _rooms     // Public immutable LiveData

    private val _isLoading = MutableLiveData<Boolean>()   // Loading state
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>() // Error message
    val errorMessage: LiveData<String?> get() = _errorMessage

    init {
        fetchRooms() // Fetch rooms on initialization
    }

    fun fetchRooms() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val response = ApiServices.roomsApiService.findAll().awaitResponse()
                if (response.isSuccessful) {
                    _rooms.value = response.body() ?: emptyList()
                } else {
                    _errorMessage.value = "Error: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Exception: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}