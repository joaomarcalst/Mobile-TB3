package com.example.automacorp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.automacorp.ui.theme.AutomacorpTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AutomacorpTheme {
                Scaffold(
                    topBar = {
                        AutomacorpTopAppBar(
                            title = null,
                            returnAction = { finish() },
                            context = this // Passa o contexto da MainActivity
                        )
                    }
                ) { innerPadding ->
                    Greeting(
                        onClick = { name ->
                            if (name.isNotBlank()) {
                                navigateToRooms(name)
                            } else {
                                Toast.makeText(
                                    this,
                                    "Please enter a room name or ID.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    // Função para navegar para a tela de Rooms
    private fun navigateToRooms(nameOrId: String = "") {
        val intent = Intent(this, RoomActivity::class.java).apply {
            putExtra(ROOM_PARAM, nameOrId.ifBlank { "Default Room" }) // Envia "Default Room" caso o campo esteja vazio
        }
        startActivity(intent)
    }

    // Função para abrir o cliente de e-mail
    private fun sendEmail() {
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("example@example.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Subject")
            putExtra(Intent.EXTRA_TEXT, "Hello, this is a test email.")
        }
        try {
            startActivity(Intent.createChooser(emailIntent, "Send Email"))
        } catch (e: Exception) {
            println("No email client installed: ${e.message}")
        }
    }

    // Função para abrir a página do GitHub
    private fun openGitHub() {
        val githubUrl = "https://github.com/joaomarcalst"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(githubUrl)
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            println("Unable to open GitHub: ${e.message}")
        }
    }

    companion object {
        const val ROOM_PARAM = "com.automacorp.room.attribute"
    }
}

@Composable
fun AppLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.ic_logo),
        contentDescription = stringResource(R.string.app_logo_description),
        modifier = modifier
            .paddingFromBaseline(top = 100.dp)
            .height(80.dp),
    )
}

@Composable
fun Greeting(onClick: (name: String) -> Unit, modifier: Modifier = Modifier) {
    var textFieldValue by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxWidth()) {
        AppLogo(Modifier.padding(top = 32.dp).align(Alignment.CenterHorizontally))
        Text(
            stringResource(R.string.act_main_welcome),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
        )
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                textFieldValue = newValue
                println("Value of the field: $newValue")
            },
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            placeholder = {
                Row {
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle,
                        modifier = Modifier.padding(end = 8.dp),
                        contentDescription = stringResource(R.string.act_main_fill_name),
                    )
                    Text(stringResource(R.string.act_main_fill_name))
                }
            }
        )
        Button(
            onClick = { onClick(textFieldValue) },
            modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(R.string.act_main_open))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AutomacorpTheme {
        Greeting(
            onClick = { name -> println("Button clicked with name: $name") },
            modifier = Modifier
        )
    }
}