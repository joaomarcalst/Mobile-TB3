package com.example.automacorp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.automacorp.model.RoomDto
import com.example.automacorp.ui.theme.AutomacorpTheme

class RoomDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val roomId = intent.getStringExtra(MainActivity.ROOM_PARAM)?.toLongOrNull()
        if (roomId == null) {
            finish() // Finaliza se o ID da sala for inválido
            return
        }

        setContent {
            AutomacorpTheme {
                val viewModel: RoomViewModel by viewModels()
                val roomState = viewModel.roomState.collectAsState()

                // Carrega os detalhes da sala
                viewModel.findRoom(roomId)

                val room = roomState.value
                if (room != null) {
                    RoomDetailScreen(
                        room = room,
                        onUpdateRoom = { updatedRoom -> viewModel.updateRoom(roomId, updatedRoom) },
                        onBackClick = { finish() }
                    )
                } else {
                    Text("Loading room details...") // Mensagem enquanto os dados são carregados
                }
            }
        }
    }
}
