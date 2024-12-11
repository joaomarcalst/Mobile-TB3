package com.example.automacorp

import android.os.Bundle
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
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.automacorp.model.RoomDto
import com.example.automacorp.service.RoomService
import com.example.automacorp.ui.theme.AutomacorpTheme


class RoomActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Obtém o parâmetro passado pela MainActivity
        val param = intent.getStringExtra(MainActivity.ROOM_PARAM)

        // Inicializa o ViewModel
        val viewModel: RoomViewModel by viewModels()
        viewModel.room = RoomService.findByNameOrId(param) // Carrega a sala no estado global do ViewModel

        // Define a ação do botão "Save Room"
        val onRoomSave: () -> Unit = {
            val room = viewModel.room
            if (room != null) {
                // Atualiza a sala no serviço
                val updatedRoom = RoomService.updateRoom(room.id, room)
                if (updatedRoom != null) {
                    println("Room updated successfully: $updatedRoom")
                } else {
                    println("Failed to update the room.")
                }
            }
        }

        // Configura o layout
        setContent {
            AutomacorpTheme {
                Scaffold(
                    floatingActionButton = { RoomUpdateButton(onRoomSave) }, // Adiciona o botão de ação flutuante
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    if (viewModel.room != null) {
                        // Passa o ViewModel para o RoomDetail
                        RoomDetail(
                            viewModel, Modifier.padding(innerPadding), RoomDto(
                                id = 1L,
                                name = "Conference Room",
                                currentTemperature = 22.5,
                                targetTemperature = 24.0,
                                windows = emptyList()
                            )
                        )
                    } else {
                        // Mostra a tela de "Nenhuma sala encontrada"
                        NoRoom(Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun RoomDetail(viewModel: RoomViewModel, modifier: Modifier = Modifier, roomDto: RoomDto) {
    val room = viewModel.room

    if (room != null) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // Nome da sala
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

            // Exibição da Temperatura Atual
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

            // Temperatura alvo e Slider
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
fun NoRoom(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.act_room_none),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
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

@Preview
@Composable
fun RoomDetailPreview() {
    AutomacorpTheme {
        RoomDetail(
            roomDto = RoomDto(
                id = 1L,
                name = "Conference Room",
                currentTemperature = 22.5,
                targetTemperature = 24.0,
                windows = emptyList()
            ),
            viewModel = TODO(),
            modifier = TODO()
        )
    }
    RoomUpdateButton(onClick = { println("Room saved!") })
}