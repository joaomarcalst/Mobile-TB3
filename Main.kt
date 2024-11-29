// Exercise 02
data class RoomDto(
    val id: Long,
    val name: String,
    val currentTemperature: Double? = null
)

fun main () {
    // Exercise 01
    println("Hello Kotlin World")

    // Exercise 03
    val rooms = listOf(
        RoomDto(1, "Room 1"),
        RoomDto(2, "Room 2", 20.3),
        RoomDto(id = 3, name = "Room 3", currentTemperature = 20.3),
        RoomDto(4, "Room 4", currentTemperature = 19.3)
    )

    // Exercise 04
    val roomNames = rooms.map { it.name }.joinToString(", ")
    println(roomNames)

    // Exercise 05
    val filteredRooms = rooms.filter { it.currentTemperature != null && it.currentTemperature!! <= 20 }

    val roomNamesFiltered = filteredRooms.map { it.name }.joinToString(", ")
    print(roomNamesFiltered)

    // Exercise 06
    var mainRoom: RoomDto? = RoomDto(5, "Room 5", currentTemperature = 19.3)

    println("\nCurrent Temperature of ${mainRoom?.name}: ${mainRoom?.currentTemperature}")

    // Exercise 07
    val room1: RoomDto? = RoomDto(1, "Room 1")
    val room2: RoomDto? = RoomDto(2, "Room 2")
    val room3: RoomDto? = RoomDto(3, "Room 3")
    val room4: RoomDto? = RoomDto(4, "Room 4")
    val room5: RoomDto? = RoomDto(5, "Room 5")

    println("Characters in room1 name: ${countRoomNameCharacters(room1)}")
    println("Characters in room2 name: ${countRoomNameCharacters(room2)}")
    println("Characters in room3 name: ${countRoomNameCharacters(room3)}")
    println("Characters in room4 name: ${countRoomNameCharacters(room4)}")
    println("Characters in room5 name: ${countRoomNameCharacters(room5)}")
}

// Exercise 07
fun countRoomNameCharacters(room: RoomDto?): Int {
    return room?.name?.length ?: 0
}


