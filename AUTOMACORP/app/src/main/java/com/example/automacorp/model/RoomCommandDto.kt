package com.automacorp.model

data class RoomCommandDto(
    val name: String,
    val currentTemperature: Double? = null,
    val targetTemperature: Double? = null,
    val floor: Int = 1,
    val buildingId: Long = -10 // Valor padrão para o ID do prédio
)