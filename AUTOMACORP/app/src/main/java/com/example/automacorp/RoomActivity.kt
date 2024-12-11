package com.example.automacorp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.automacorp.model.RoomDto
import com.example.automacorp.service.RoomService
import com.example.automacorp.ui.theme.AutomacorpTheme

class RoomActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Obtém o parâmetro passado para identificar a sala
        val param = intent.getStringExtra(MainActivity.ROOM_PARAM) ?: ""
        val viewModel: RoomViewModel by viewModels()

        // Busca a sala e configura o ViewModel
        viewModel.room = RoomService.findByNameOrId(param) ?: run {
            Toast.makeText(this, "Room not found: $param", Toast.LENGTH_SHORT).show()
            finish() // Encerra a Activity se a sala não for encontrada
            return
        }

        // Configura a ação do botão "Save Room"
        val onRoomSave: () -> Unit = {
            val room = viewModel.room
            if (room != null) {
                try {
                    val updatedRoom = RoomService.updateRoom(room.id, room)
                    if (updatedRoom != null) {
                        Toast.makeText(this, "Room updated successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to update the room.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error saving room: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val navigateBack: () -> Unit = {
            finish() // Encerra a Activity e volta para a anterior
        }

        // Configura o layout da Activity
        setContent {
            AutomacorpTheme {
                Scaffold(
                    topBar = {
                        AutomacorpTopAppBar(
                            title = "ROOMS",
                            returnAction = { finish() },
                            context = this // Passa o contexto da RoomActivity
                        )
                    },
                    floatingActionButton = { RoomUpdateButton(onRoomSave) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    RoomContent(viewModel, Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun RoomContent(viewModel: RoomViewModel, modifier: Modifier = Modifier) {
    val room = viewModel.room
    if (room != null) {
        RoomDetail(viewModel, modifier)
    } else {
        NoRoom(RoomService.findAll(), modifier)
    }
}

@Composable
fun RoomDetail(viewModel: RoomViewModel, modifier: Modifier = Modifier) {
    val room = viewModel.room

    if (room != null) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.act_room_name),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            OutlinedTextField(
                value = room.name,
                onValueChange = { viewModel.room = room.copy(name = it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.act_room_name)) }
            )

            Text(
                text = stringResource(R.string.act_room_current_temperature),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = room.currentTemperature?.let { "$it°C" } ?: "N/A",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = stringResource(R.string.act_room_target_temperature),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Slider(
                value = room.targetTemperature?.toFloat() ?: 18.0f,
                onValueChange = { viewModel.room = room.copy(targetTemperature = it.toDouble()) },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                steps = 0,
                valueRange = 10f..28f
            )
            Text(
                text = "Target: ${(room.targetTemperature ?: 18.0).toString()}°C",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun NoRoom(rooms: List<RoomDto>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.act_room_none),
            style = MaterialTheme.typography.headlineMedium
        )

        if (rooms.isNotEmpty()) {
            Text(
                text = "Available Rooms:",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
            rooms.forEach { room ->
                Text(
                    text = "- ${room.name}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun RoomUpdateButton(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        icon = {
            Icon(
                Icons.Filled.Done,
                contentDescription = stringResource(R.string.act_room_save),
            )
        },
        text = { Text(text = stringResource(R.string.act_room_save)) }
    )
}