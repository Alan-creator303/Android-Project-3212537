package com.example.shakeit

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Purple = Color(0xFF8B5CF6)
private val Ink = Color(0xFF211A2E)
private val Muted = Color(0xFF777183)
private val Page = Color(0xFFF8F6FC)
private val Line = Color(0xFFE5E1EB)


class DecisionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Read the list of options that the previous screen sent us, and which
        // page should be shown first (0 coin, 1 dice, 2 wheel, 3 slot).
        val options: List<String> = intent.getStringArrayListExtra(EXTRA_OPTIONS) ?: emptyList()
        val startPage = intent.getIntExtra(EXTRA_START_PAGE, 0)

        setContent {
            MaterialTheme {
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

    fun pickOption() {
        if (options.isNotEmpty()) {
            result = options.random()
        }
    }


    // Opens the email app with the result already written in the message.
    fun sendEmail() {
        if (result.isEmpty()) {
            return
        }
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Shake It result")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Shake It! picked: $result")
        try {
            context.startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            // there is no email app on this phone
        }
    }

    // Opens the messages app with the result already written.
    fun sendMessage() {
        if (result.isEmpty()) {
            return
        }
        val messageIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"))
        messageIntent.putExtra("sms_body", "Shake It! picked: $result")
        try {
            context.startActivity(messageIntent)
        } catch (e: ActivityNotFoundException) {
            // there is no messages app on this phone
        }
    }

    Scaffold(containerColor = Page) { innerPadding ->
        // Only one decision screen is shown, the one the user picked before.
        Box(modifier = Modifier.padding(innerPadding)) {
            when (startPage) {
                0 -> CoinScreen(options, result, onBack, onPick = { pickOption() }, onShare = { showShareDialog = true })
                1 -> DiceScreen(options, result, onBack, onPick = { pickOption() }, onShare = { showShareDialog = true })
                2 -> WheelScreen(options, result, onBack, onPick = { pickOption() }, onShare = { showShareDialog = true })
                else -> SlotScreen(options, result, onBack, onPick = { pickOption() }, onShare = { showShareDialog = true })
            }
        }

        // Small dialog with the two ways of sharing the result.
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

@Composable
private fun CoinScreen(
    options: List<String>,
    result: String,
    onBack: () -> Unit,
    onPick: () -> Unit,
    onShare: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar("Coin Flip", onBack)

        Spacer(modifier = Modifier.height(26.dp))

        // The two options, one for each side of the coin.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            options.take(2).forEach { option ->
                OptionCard(option, Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(34.dp))


        Box(
            modifier = Modifier
                .size(170.dp)
                .background(Color(0xFFFFC94D), CircleShape)
                .border(6.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (result.isEmpty()) "?" else result,
                modifier = Modifier.padding(horizontal = 12.dp),
                color = Ink,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        DecisionResult(emoji = "🪙", value = result)

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onPick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text("Flip", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(10.dp))
        ShareButton(onShare = onShare, enabled = result.isNotEmpty())
    }
}

@Composable
private fun DiceScreen(
    options: List<String>,
    result: String,
    onBack: () -> Unit,
    onPick: () -> Unit,
    onShare: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar("Dice Roll", onBack)

        Spacer(modifier = Modifier.height(24.dp))

        // The dice in the middle of the screen.
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color.White, RoundedCornerShape(28.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("🎲", fontSize = 64.sp)
        }

        Spacer(modifier = Modifier.height(26.dp))

        OptionChips(options)

        Spacer(modifier = Modifier.height(22.dp))

        DecisionResult(emoji = "🎲", value = result)

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onPick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text("Roll", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(10.dp))
        ShareButton(onShare = onShare, enabled = result.isNotEmpty())
    }
}

@Composable
private fun WheelScreen(
    options: List<String>,
    result: String,
    onBack: () -> Unit,
    onPick: () -> Unit,
    onShare: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar("Spinner Wheel", onBack)

        Spacer(modifier = Modifier.height(18.dp))

        OptionChips(options)

        Spacer(modifier = Modifier.height(16.dp))

        // The little arrow that points at the slice the wheel stops on.
        Text("▼", color = Purple, fontSize = 18.sp)

        WheelDrawing()

        Spacer(modifier = Modifier.height(16.dp))

        DecisionResult(emoji = "🎡", value = result)

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onPick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text("Spin", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(10.dp))
        ShareButton(onShare = onShare, enabled = result.isNotEmpty())
    }
}

@Composable
private fun SlotScreen(
    options: List<String>,
    result: String,
    onBack: () -> Unit,
    onPick: () -> Unit,
    onShare: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar("Slot Machine", onBack)

        Spacer(modifier = Modifier.height(26.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1F2937), RoundedCornerShape(22.dp))
                .padding(16.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(3.dp).background(Color(0xFFEF4444)))
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                options.take(3).forEach { option ->
                    SlotReel(option, Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier.fillMaxWidth().height(3.dp).background(Color(0xFFEF4444)))
        }

        Spacer(modifier = Modifier.height(24.dp))

        DecisionResult(emoji = "🎰", value = result)

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onPick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text("Go", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(10.dp))
        ShareButton(onShare = onShare, enabled = result.isNotEmpty())
    }
}

@Composable
private fun TopBar(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "‹",
            modifier = Modifier
                .clickable { onBack() }
                .padding(horizontal = 6.dp),
            color = Purple,
            fontSize = 34.sp
        )
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = Ink,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(28.dp))
    }
}

// The purple box that shows what the app picked.
@Composable
private fun DecisionResult(emoji: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Purple, RoundedCornerShape(22.dp))
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$emoji RESULT",
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (value.isEmpty()) "Not decided yet" else value,
            modifier = Modifier.padding(horizontal = 12.dp),
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
private fun ShareButton(onShare: () -> Unit, enabled: Boolean) {
    Button(
        onClick = onShare,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        enabled = enabled,
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Purple,
            disabledContainerColor = Color.White,
            disabledContentColor = Color(0xFFB9B3C6)
        )
    ) {
        Text("Share Result", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
    }
}


@Composable
private fun OptionCard(text: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .border(1.dp, Line, RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            color = Ink,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
private fun OptionChips(options: List<String>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(options) { option ->
            Text(
                text = option,
                modifier = Modifier
                    .border(1.dp, Line, RoundedCornerShape(16.dp))
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                color = Ink,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun SlotReel(text: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(14.dp))
            .padding(horizontal = 6.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            color = Ink,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
private fun WheelDrawing() {
    val sliceColors = listOf(
        Color(0xFF8B5CF6),
        Color(0xFFF472B6),
        Color(0xFF34D399),
        Color(0xFFFBBF24),
        Color(0xFF60A5FA),
        Color(0xFFFB7185)
    )

    Canvas(modifier = Modifier.size(210.dp)) {
        val slice = 360f / sliceColors.size
        sliceColors.forEachIndexed { index, color ->
            drawArc(
                color = color,
                startAngle = index * slice,
                sweepAngle = slice,
                useCenter = true
            )
        }
        // A purple circle with a white circle inside, like the pin of the wheel.
        drawCircle(color = Purple, radius = size.minDimension / 8f)
        drawCircle(color = Color.White, radius = size.minDimension / 11f)
    }
}

@Preview(showBackground = true)
@Composable
private fun CoinScreenPreview() {
    MaterialTheme {
        Surface(color = Page) {
            CoinScreen(
                options = listOf("🍕 Pizza", "🍔 Burger"),
                result = "🍕 Pizza",
                onBack = { },
                onPick = { },
                onShare = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DiceScreenPreview() {
    MaterialTheme {
        Surface(color = Page) {
            DiceScreen(
                options = listOf("🍕 Pizza", "🍔 Burger", "🥩 Steak"),
                result = "",
                onBack = { },
                onPick = { },
                onShare = { }
            )
        }
    }
}
