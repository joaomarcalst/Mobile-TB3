package com.example.automacorp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.automacorp.model.RoomDto
import com.example.automacorp.ui.theme.AutomacorpTheme
import kotlin.math.round

class RoomActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val roomId = intent.getStringExtra(MainActivity.ROOM_PARAM)?.toLongOrNull()
        if (roomId == null) {
            Toast.makeText(this, "Invalid room ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setContent {
            AutomacorpTheme {
                val viewModel: RoomViewModel by viewModels()
                viewModel.findRoom(roomId) // Certifique-se de que esta chamada está sendo feita

                val roomState by viewModel.roomState.collectAsState()

                if (roomState != null) {
                    RoomDetailScreen(
                        room = roomState!!,
                        onUpdateRoom = { updatedRoom -> viewModel.updateRoom(roomId, updatedRoom) },
                        onBackClick = { finish() }
                    )
                } else {
                    Text("Loading room details...") // Exibido enquanto roomState for null
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomDetailScreen(
    room: RoomDto,
    onUpdateRoom: (RoomDto) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = room.name) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            // Passa os valores de padding diretamente para o conteúdo
            RoomDetailContent(
                room = room,
                modifier = Modifier.padding(paddingValues),
                onUpdateRoom = onUpdateRoom
            )
        }
    )
}

@Composable
fun RoomDetailContent(
    room: RoomDto,
    modifier: Modifier = Modifier,
    onUpdateRoom: (RoomDto) -> Unit
) {
    // Gerencia o estado local do quarto
    var roomState by remember { mutableStateOf(room) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Nome da Sala
        Text("Room Name: ${roomState.name}", style = MaterialTheme.typography.bodyLarge)

        // Temperatura Atual
        Text(
            text = "Current Temperature: ${roomState.currentTemperature ?: "N/A"}°C",
            style = MaterialTheme.typography.bodyLarge
        )

        // Controle de Temperatura Alvo com Slider
        Text(
            text = "Target Temperature: ${roomState.targetTemperature ?: "N/A"}°C",
            style = MaterialTheme.typography.bodyLarge
        )
        Slider(
            value = roomState.targetTemperature?.toFloat() ?: 18.0f,
            onValueChange = { newValue ->
                roomState = roomState.copy(targetTemperature = newValue.toDouble())
            },
            onValueChangeFinished = {
                onUpdateRoom(roomState) // Atualiza o estado do quarto
            },
            valueRange = 10f..28f,
            steps = 18,
            modifier = Modifier.fillMaxWidth()
        )
    }
}