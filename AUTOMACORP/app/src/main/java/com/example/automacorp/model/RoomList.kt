package com.example.automacorp.model

data class RoomList(
    val room: List<RoomDto> = emptyList(),
    val error: String? = null
) : List<RoomDto> by room