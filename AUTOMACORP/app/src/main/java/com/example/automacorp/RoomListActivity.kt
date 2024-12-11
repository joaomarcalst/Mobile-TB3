package com.example.automacorp


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.automacorp.model.RoomDto
import com.example.automacorp.ui.theme.AutomacorpTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: RoomViewModel by viewModels()

        setContent {
            val roomsState = viewModel.roomsState.collectAsState()
            val context = LocalContext.current
            AutomacorpTheme {
                RoomList(
                    rooms = roomsState.value, // Correctly use the state value
                    navigateBack = { finish() },
                    openRoom = { roomId ->
                        val intent = Intent(this, RoomActivity::class.java).apply {
                            putExtra(MainActivity.ROOM_PARAM, roomId.toString())
                        }
                        startActivity(intent)
                    },
                    context = context
                )
            }
        }

        // Fetch the room list using a coroutine
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.findAll() // Ensure this fetches the rooms from the API
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomList(
    rooms: List<RoomDto>,
    navigateBack: () -> Unit,
    openRoom: (Long) -> Unit,
    context: Context // Ensure this parameter is present
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rooms") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (rooms.isEmpty()) {
            Text(
                text = "No rooms available",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                textAlign = TextAlign.Center
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(innerPadding)
            ) {
                items(rooms, key = { it.id }) { room ->
                    RoomItem(
                        room = room,
                        modifier = Modifier.clickable { openRoom(room.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun RoomItem(room: RoomDto, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = room.name, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "Current Temperature: ${room.currentTemperature ?: "?"}°C",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Target Temperature: ${room.targetTemperature ?: "?"}°C",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}