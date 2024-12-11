package com.example.automacorp.service

import androidx.core.text.isDigitsOnly
import com.example.automacorp.model.RoomDto
import com.example.automacorp.model.WindowDto
import com.example.automacorp.model.WindowStatus

object RoomService {
    val ROOM_KIND: List<String> = listOf("Room", "Meeting", "Laboratory", "Office", "Boardroom")
    val ROOM_NUMBER: List<Char> = ('A'..'Z').toList()
    val WINDOW_KIND: List<String> = listOf("Sliding", "Bay", "Casement", "Hung", "Fixed")

    // Gera uma janela com dados aleatórios
    fun generateWindow(id: Long, roomId: Long, roomName: String): WindowDto {
        return WindowDto(
            id = id,
            name = "${WINDOW_KIND.random()} Window $id",
            roomName = roomName,
            roomId = roomId,
            windowStatus = WindowStatus.values().random()
        )
    }

    // Gera uma sala com dados aleatórios
    fun generateRoom(id: Long): RoomDto {
        val roomName = "${ROOM_NUMBER.random()}$id ${ROOM_KIND.random()}" // Nome aleatório da sala
        val windows = (1..(1..6).random()).map { generateWindow(it.toLong(), id, roomName) } // Janelas aleatórias
        val room = RoomDto(
            id = id,
            name = roomName,
            currentTemperature = (15..30).random().toDouble(), // Temperatura atual aleatória
            targetTemperature = (15..22).random().toDouble(), // Temperatura alvo aleatória
            windows = windows
        )
        println("Generated room: $room") // Log para verificar a geração
        return room
    }

    // Lista de salas geradas
    val ROOMS = (1..50).map {
        val room = generateRoom(it.toLong())
        println("Adding room to list: $room") // Log para depuração
        room
    }.toMutableList()

    // Retorna todas as salas
    fun findAll(): List<RoomDto> {
        return ROOMS.sortedBy { it.name }
    }

    // Encontra uma sala pelo ID
    fun findById(id: Long): RoomDto? {
        println("Searching for room by ID: $id") // Log para depuração
        return ROOMS.find { it.id == id }
    }

    // Encontra uma sala pelo nome
    fun findByName(name: String): RoomDto? {
        println("Searching for room by name: $name") // Log para depuração
        return ROOMS.find { it.name.equals(name, ignoreCase = true) }
    }

    // Atualiza uma sala existente
    fun updateRoom(id: Long, room: RoomDto): RoomDto? {
        println("Updating room with ID: $id") // Log para depuração
        val index = ROOMS.indexOfFirst { it.id == id }
        if (index == -1) {
            println("Room not found for update!") // Log de erro
            return null
        }
        val updatedRoom = room.copy(id = id)
        ROOMS[index] = updatedRoom
        println("Updated room: $updatedRoom") // Log para confirmar a atualização
        return updatedRoom
    }

    // Encontra uma sala pelo nome ou ID
    fun findByNameOrId(nameOrId: String?): RoomDto? {
        if (nameOrId.isNullOrBlank()) {
            // Retorna um valor padrão ou nulo caso o parâmetro seja inválido
            return null
        }

        return if (nameOrId.isDigitsOnly()) {
            try {
                findById(nameOrId.toLong())
            } catch (e: NumberFormatException) {
                null // Retorna null caso a conversão falhe
            }
        } else {
            findByName(nameOrId)
        }
    }

    // Adiciona uma nova sala à lista
    fun addRoom(room: RoomDto): RoomDto {
        println("Adding new room: $room") // Log para depuração
        if (ROOMS.any { it.id == room.id }) {
            throw IllegalArgumentException("Room with ID ${room.id} already exists")
        }
        ROOMS.add(room)
        println("Room added successfully!") // Log para confirmar a adição
        return room
    }
}