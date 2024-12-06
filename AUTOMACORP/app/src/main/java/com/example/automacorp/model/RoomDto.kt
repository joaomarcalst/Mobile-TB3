package com.example.automacorp.model

data class RoomDto(
    val id: Long,
    val name: String,
    val currentTemperature: Number?,
    val targetTemperature: Number?,
    val windows: List<WindowDto>,
)