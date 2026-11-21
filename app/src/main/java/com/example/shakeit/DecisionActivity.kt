package com.example.shakeit

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
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
import kotlin.math.sqrt

class DecisionActivity : ComponentActivity() {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var lastShakeTime = 0L

    private var options: List<String> = emptyList()
    private var startPage = 0

    // the result is kept here, so the sensor can change it as well as the buttons
    private var result by mutableStateOf("")

    // the listener for the accelerometer, same way as the sensors tutorial
    private val sensorEventListener: SensorEventListener = object : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {

                var x = event.values[0]
                var y = event.values[1]
                var z = event.values[2]

                // take gravity away, so only the movement of the phone is left
                val force = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH

                if (force > 8f) {
                    val now = System.currentTimeMillis()
                    // wait 1 second, so one shake is not counted two or three times
                    if (now - lastShakeTime > 1000) {
                        lastShakeTime = now
                        pickOption()
                    }
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        options = intent.getStringArrayListExtra(EXTRA_OPTIONS) ?: emptyList()
        startPage = intent.getIntExtra(EXTRA_START_PAGE, 0)

        // get the accelerometer, not every phone has one
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        setContent {
            ShakeItTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Page
                ) {
                    DecisionScreens(
                        options = options,
                        startPage = startPage,
                        result = result,
                        onBack = { finish() },
                        onPick = { pickOption() }
                    )
                }
            }
        }
    }

    private fun pickOption() {
        // only one decision on each screen, so shaking twice does nothing
        if (options.isNotEmpty() && result.isEmpty()) {
            result = options.random()
        }
    }

    override fun onResume() {
        super.onResume()
        // start listening when the screen is visible
        if (accelerometer != null) {
            sensorManager.registerListener(
                sensorEventListener,
                accelerometer,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    override fun onPause() {
        super.onPause()
        // stop listening when the screen is not visible, to save battery
        sensorManager.unregisterListener(sensorEventListener)
    }

    companion object {

        const val EXTRA_OPTIONS = "extra_options"
        const val EXTRA_START_PAGE = "extra_start_page"
    }
}

@Composable
private fun DecisionScreens(
    options: List<String>,
    startPage: Int,
    result: String,
    onBack: () -> Unit,
    onPick: () -> Unit
) {
    val context = LocalContext.current

    var showShareDialog by remember { mutableStateOf(false) }

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
                    onPick = onPick,
                    onShare = { showShareDialog = true }
                )

                1 -> DiceScreen(
                    options = options,
                    result = result,
                    onBack = onBack,
                    onPick = onPick,
                    onShare = { showShareDialog = true }
                )

                2 -> WheelScreen(
                    options = options,
                    result = result,
                    onBack = onBack,
                    onPick = onPick,
                    onShare = { showShareDialog = true }
                )

                else -> SlotScreen(
                    options = options,
                    result = result,
                    onBack = onBack,
                    onPick = onPick,
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
