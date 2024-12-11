package com.example.automacorp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.automacorp.model.RoomDto
import com.example.automacorp.service.RoomService
import com.example.automacorp.ui.theme.AutomacorpTheme

class RoomListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navigateBack: () -> Unit = { finish() } // Ação para o botão "Voltar"

        setContent {
            AutomacorpTheme {
                Scaffold(
                    topBar = {
                        AutomacorpTopAppBar(
                            title = "Room List",
                            returnAction = navigateBack,
                            context = this // Passa o contexto necessário para os intents
                        )
                    }
                ) { innerPadding ->
                    RoomListScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun RoomListScreen(modifier: Modifier = Modifier) {
    val rooms = RoomService.findAll()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (rooms.isEmpty()) {
            item {
                Text(text = "No rooms available")
            }
        } else {
            items(rooms) { room ->
                RoomItem(room)
            }
        }
    }
}

@Composable
fun RoomItem(room: RoomDto) {
    Text(
        text = "Room Name: ${room.name}, Target Temp: ${room.targetTemperature}°C",
        modifier = Modifier.padding(vertical = 8.dp)
    )
}