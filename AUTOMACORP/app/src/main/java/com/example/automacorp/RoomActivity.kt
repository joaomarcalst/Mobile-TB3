    package com.example.automacorp

    import android.os.Bundle
    import kotlin.math.round
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.activity.enableEdgeToEdge
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.OutlinedTextField
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Slider
    import androidx.compose.material3.SliderDefaults
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
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
            val param = intent.getStringExtra(MainActivity.ROOM_PARAM)
            val room = RoomService.findByNameOrId(param)

            setContent {
                AutomacorpTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        if (room != null) {
                            RoomDetail(room, Modifier.padding(innerPadding))
                        } else {
                            NoRoom(Modifier.padding(innerPadding))
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun RoomDetail(roomDto: RoomDto, modifier: Modifier = Modifier) {
        var room by remember { mutableStateOf(roomDto) }

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
                onValueChange = { room = room.copy(name = it) },
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
                onValueChange = { room = room.copy(targetTemperature = it.toDouble()) },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                steps = 0,
                valueRange = 10f..28f
            )
            Text(
                text = "Target: ${(round((room.targetTemperature?.toDouble()?: 18.0) * 10) / 10)}°C",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
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

    @Preview(showBackground = true)
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
                )
            )
        }
    }