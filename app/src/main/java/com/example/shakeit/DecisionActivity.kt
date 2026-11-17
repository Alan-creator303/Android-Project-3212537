package com.example.shakeit

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.shakeit.ui.screens.CoinScreen
import com.example.shakeit.ui.screens.DiceScreen
import com.example.shakeit.ui.screens.SlotScreen
import com.example.shakeit.ui.screens.WheelScreen
import com.example.shakeit.ui.theme.Page
import com.example.shakeit.ui.theme.ShakeItTheme

class DecisionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val options: List<String> = intent.getStringArrayListExtra(EXTRA_OPTIONS) ?: emptyList()
        val startPage = intent.getIntExtra(EXTRA_START_PAGE, 0)

        setContent {
            ShakeItTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Page
                ) {
                    DecisionScreens(options, startPage, onBack = { finish() })
                }
            }
        }
    }

    companion object {

        const val EXTRA_OPTIONS = "extra_options"
        const val EXTRA_START_PAGE = "extra_start_page"
    }
}

@Composable
private fun DecisionScreens(options: List<String>, startPage: Int, onBack: () -> Unit) {
    val context = LocalContext.current


    var result by remember { mutableStateOf("") }
    var showShareDialog by remember { mutableStateOf(false) }

    fun pickOption() {  //pick a random option
        if (options.isNotEmpty()) {
            result = options.random()
        }
    }


    fun sendEmail() {  //opens the email app with the result
        if (result.isEmpty()) {
            return
        }
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Shake It result")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Shake It! picked: $result")
        try {
            context.startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
        }
    }

    fun sendMessage() {  //opens the messages app with the result
        if (result.isEmpty()) {
            return
        }
        val messageIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"))
        messageIntent.putExtra("sms_body", "Shake It! picked: $result")
        try {
            context.startActivity(messageIntent)
        } catch (e: ActivityNotFoundException) {
        }
    }

    Scaffold(containerColor = Page) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (startPage) {
                0 -> CoinScreen(
                    options = options,
                    result = result,
                    onBack = onBack,
                    onPick = { pickOption() },
                    onShare = { showShareDialog = true }
                )

                1 -> DiceScreen(
                    options = options,
                    result = result,
                    onBack = onBack,
                    onPick = { pickOption() },
                    onShare = { showShareDialog = true }
                )

                2 -> WheelScreen(
                    options = options,
                    result = result,
                    onBack = onBack,
                    onPick = { pickOption() },
                    onShare = { showShareDialog = true }
                )

                else -> SlotScreen(
                    options = options,
                    result = result,
                    onBack = onBack,
                    onPick = { pickOption() },
                    onShare = { showShareDialog = true }
                )
            }
        }

        if (showShareDialog) {
            AlertDialog(
                onDismissRequest = { showShareDialog = false },
                title = { Text("Share Result") },
                text = { Text("Send it by email or by message?") },
                confirmButton = {
                    TextButton(onClick = {
                        showShareDialog = false
                        sendEmail()
                    }) {
                        Text("Email")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showShareDialog = false
                        sendMessage()
                    }) {
                        Text("Message")
                    }
                }
            )
        }
    }
}
