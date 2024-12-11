package com.example.automacorp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AutomacorpTopAppBar(
    title: String? = null,
    returnAction: () -> Unit = {},
    context: Context // Contexto necessário para os Intents
) {
    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.primary,
    )

    val actions: @Composable RowScope.() -> Unit = {
        IconButton(onClick = {
            // Intent para abrir RoomListActivity
            val intent = Intent(context, RoomListActivity::class.java)
            context.startActivity(intent)
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_action_rooms),
                contentDescription = stringResource(R.string.app_go_room_description)
            )
        }
        IconButton(onClick = {
            // Intent para enviar e-mail
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:example@example.com")
                putExtra(Intent.EXTRA_SUBJECT, "Contact Automacorp")
            }
            try {
                context.startActivity(emailIntent)
            } catch (e: Exception) {
                Toast.makeText(context, "Error opening email client: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_action_mail),
                contentDescription = stringResource(R.string.app_go_mail_description)
            )
        }
        IconButton(onClick = {
            // Intent para abrir a página do GitHub
            val githubIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/joaomarcalst"))
            try {
                context.startActivity(githubIntent)
            } catch (e: Exception) {
                Toast.makeText(context, "Error opening GitHub: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_action_git),
                contentDescription = stringResource(R.string.app_go_github_description)
            )
        }
    }

    if (title == null) {
        TopAppBar(
            title = { Text("") },
            colors = colors,
            actions = actions
        )
    } else {
        MediumTopAppBar(
            title = { Text(title) },
            colors = colors,
            navigationIcon = {
                IconButton(onClick = returnAction) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.app_go_back_description)
                    )
                }
            },
            actions = actions
        )
    }
}