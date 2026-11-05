package com.example.shakeit

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

private val Purple = Color(0xFF8B5CF6)
private val Ink = Color(0xFF211A2E)
private val Muted = Color(0xFF777183)
private val Page = Color(0xFFF8F6FC)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val pagerState = rememberPagerState(pageCount = { 7 })
                val scope = rememberCoroutineScope()

                // The options the user picked, the options the user typed in,
                // and the text that is currently inside the text field.
                var selectedOptions by remember { mutableStateOf(listOf<String>()) }
                var customOptions by remember { mutableStateOf(listOf<String>()) }
                var customText by remember { mutableStateOf("") }

                // Move the pager to another page, for example when a button is pressed.
                fun goToPage(page: Int) {
                    scope.launch { pagerState.animateScrollToPage(page) }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Page
                ) {
                    // The Scaffold gives us the innerPadding, so our screens
                    // stay below the status bar and above the navigation bar.
                    Scaffold(containerColor = Page) { innerPadding ->
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.padding(innerPadding)
                        ) { page ->
                            when (page) {
                                0 -> StartScreen(
                                    onStart = { goToPage(1) },
                                    onHistory = { goToPage(6) }
                                )

                                1 -> OptionSelectionScreen(
                                    selectedOptions = selectedOptions,
                                    customOptions = customOptions,
                                    customText = customText,
                                    onToggle = { option ->
                                        selectedOptions = toggleOption(selectedOptions, option)
                                    },
                                    onCustomTextChange = { customText = it },
                                    onAddCustom = {
                                        if (customText.isNotBlank()) {
                                            customOptions = customOptions + customText.trim()
                                            customText = ""
                                        }
                                    },
                                    onNext = { goToPage(2) }
                                )

                                2 -> CoinScreen()
                                3 -> DecisionScreen()
                                4 -> WheelScreen()
                                5 -> SlotScreen()
                                else -> HistoryScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StartScreen(onStart: () -> Unit, onHistory: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Shake It!",
            color = Ink,
            fontSize = 38.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Help you make choices.",
            color = Muted,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))
        DiceIllustration()
        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text("Start Your Decision", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(14.dp))
        Button(
            onClick = onHistory,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Purple
            )
        ) {
            Text("View History", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun OptionSelectionScreen(
    selectedOptions: List<String>,
    customOptions: List<String>,
    customText: String,
    onToggle: (String) -> Unit,
    onCustomTextChange: (String) -> Unit,
    onAddCustom: () -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("‹", modifier = Modifier.padding(horizontal = 10.dp), color = Purple, fontSize = 38.sp)
            Text(
                text = "Select Options",
                modifier = Modifier.weight(1f),
                color = Ink,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(42.dp))
        }

        Text(
            text = "Choose at least 2 options",
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
            color = Muted,
            fontSize = 14.sp
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OptionCategory(
                    title = "Sports",
                    options = listOf(
                        "🏃 Running", "🏀 Basketball", "⚽ Soccer", "🏋️ Fitness",
                        "🏊 Swimming", "🏸 Badminton", "🚴 Cycling", "🎾 Tennis", "🏓 Table Tennis"
                    ),
                    selectedOptions = selectedOptions,
                    onToggle = onToggle
                )
            }
            item {
                OptionCategory(
                    title = "Food",
                    options = listOf(
                        "🍔 Burger", "🍟 Fries", "🥩 Steak", "🥤 Cola",
                        "🥪 Sandwich", "🍕 Pizza", "🍜 Noodles", "🧃 Juice"
                    ),
                    selectedOptions = selectedOptions,
                    onToggle = onToggle
                )
            }
            item {
                OptionCategory(
                    title = "Daily Tasks",
                    options = listOf(
                        "📚 Homework", "📖 Reading", "🎉 Party", "🛌 Rest"
                    ),
                    selectedOptions = selectedOptions,
                    onToggle = onToggle
                )
            }
            item {
                CustomOptionSection(
                    customOptions = customOptions,
                    selectedOptions = selectedOptions,
                    customText = customText,
                    onToggle = onToggle,
                    onCustomTextChange = onCustomTextChange,
                    onAddCustom = onAddCustom
                )
            }
        }

        Surface(color = Color.White) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Selected: ${selectedOptions.size}",
                    modifier = Modifier.padding(bottom = 10.dp),
                    color = Ink,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Button(
                    onClick = onNext,
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    // The button only works when at least 2 options are selected.
                    enabled = selectedOptions.size >= 2,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple,
                        disabledContainerColor = Color(0xFFD8D1E5)
                    )
                ) {
                    Text("Next", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun OptionCategory(
    title: String,
    options: List<String>,
    selectedOptions: List<String>,
    onToggle: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 18.dp)) {
        Text(title, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(options) { option ->
                OptionChip(
                    option = option,
                    isSelected = selectedOptions.contains(option),
                    onClick = { onToggle(option) }
                )
            }
        }
    }
}

@Composable
private fun CustomOptionSection(
    customOptions: List<String>,
    selectedOptions: List<String>,
    customText: String,
    onToggle: (String) -> Unit,
    onCustomTextChange: (String) -> Unit,
    onAddCustom: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Custom", color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Text("add your own idea", color = Purple, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(10.dp))

        // The options the user added. They can be picked like the other ones.
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(customOptions) { option ->
                OptionChip(
                    option = option,
                    isSelected = selectedOptions.contains(option),
                    onClick = { onToggle(option) }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = customText,
                onValueChange = onCustomTextChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                label = { Text("Add your own option...") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onAddCustom,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple)
            ) {
                Text("Add")
            }
        }
    }
}

@Composable
private fun OptionChip(option: String, isSelected: Boolean, onClick: () -> Unit) {
    Text(
        text = option,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = if (isSelected) Purple else Color(0xFFE5E1EB),
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = if (isSelected) Color(0xFFF3EEFE) else Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        color = if (isSelected) Purple else Ink,
        fontSize = 13.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
    )
}

// Adds the option to the list, or takes it out when it is already in the list.
private fun toggleOption(options: List<String>, option: String): List<String> {
    return if (options.contains(option)) {
        options - option
    } else {
        options + option
    }
}

// The third screen: the coin flip. It is used when the user picks
// only two options, so the coin has two sides.
@Composable
private fun CoinScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("‹", modifier = Modifier.padding(horizontal = 6.dp), color = Purple, fontSize = 34.sp)
            Text(
                text = "Coin Flip",
                modifier = Modifier.weight(1f),
                color = Ink,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(28.dp))
        }

        Spacer(modifier = Modifier.height(26.dp))

        // The two options, one for each side of the coin.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OptionCard("🍕", "Pizza", Modifier.weight(1f))
            OptionCard("🍔", "Burger", Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(34.dp))

        // The coin: a big yellow circle with the side it lands on.
        Box(
            modifier = Modifier
                .size(170.dp)
                .background(Color(0xFFFFC94D), CircleShape)
                .border(6.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🍕", fontSize = 52.sp)
                Text("Pizza", color = Ink, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        DecisionResult(emoji = "🪙", value = "Pizza")

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text("Flip", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Purple
            )
        ) {
            Text("Share Result", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}


@Composable
private fun DecisionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("‹", modifier = Modifier.padding(horizontal = 6.dp), color = Purple, fontSize = 34.sp)
            Text(
                text = "Dice Roll",
                modifier = Modifier.weight(1f),
                color = Ink,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(28.dp))
        }


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

        // The options the user picked, three small cards in one row.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OptionCard("🍕", "Pizza", Modifier.weight(1f))
            OptionCard("🍔", "Burger", Modifier.weight(1f))
            OptionCard("🥩", "Steak", Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(22.dp))

        // The purple box that will show the result.
        DecisionResult(emoji = "🎲", value = "Pizza")

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text("Roll", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Purple
            )
        ) {
            Text("Share Result", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun OptionCard(emoji: String, name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .border(1.dp, Color(0xFFE5E1EB), RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Text(name, color = Ink, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

// The purple box that shows what the app picked. All three decision
// screens use it, only the emoji and the value change.
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
            text = value,
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

// The spinner wheel screen. Same idea as the dice screen, but the
// "random thing" in the middle is a colorful wheel.
@Composable
private fun WheelScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("‹", modifier = Modifier.padding(horizontal = 6.dp), color = Purple, fontSize = 34.sp)
            Text(
                text = "Spinner Wheel",
                modifier = Modifier.weight(1f),
                color = Ink,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(28.dp))
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OptionCard("🍕", "Pizza", Modifier.weight(1f))
            OptionCard("🍔", "Burger", Modifier.weight(1f))
            OptionCard("🥩", "Steak", Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // The little arrow that points at the slice the wheel stops on.
        Text("▼", color = Purple, fontSize = 18.sp)

        WheelDrawing()

        Spacer(modifier = Modifier.height(16.dp))

        DecisionResult(emoji = "🎡", value = "Pizza")

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text("Spin", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Purple
            )
        ) {
            Text("Share Result", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

// The wheel itself: 6 colored slices, drawn with Canvas.
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
        // A small white circle in the middle, like the pin of the wheel.
        // A purple circle with a white circle inside, so it looks like a wheel pin.
        drawCircle(color = Purple, radius = size.minDimension / 8f)
        drawCircle(color = Color.White, radius = size.minDimension / 11f)
    }
}

// The slot machine screen: three reels inside a dark box.
@Composable
private fun SlotScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("‹", modifier = Modifier.padding(horizontal = 6.dp), color = Purple, fontSize = 34.sp)
            Text(
                text = "Slot Machine",
                modifier = Modifier.weight(1f),
                color = Ink,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(28.dp))
        }

        Spacer(modifier = Modifier.height(26.dp))

        // The machine: a dark box with the three reels inside,
        // and two red lines like the lines of a real slot machine.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1F2937), RoundedCornerShape(22.dp))
                .padding(16.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(3.dp).background(Color(0xFFEF4444)))
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SlotReel("🍕", "Pizza", Modifier.weight(1f))
                SlotReel("🍔", "Burger", Modifier.weight(1f))
                SlotReel("🥩", "Steak", Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier.fillMaxWidth().height(3.dp).background(Color(0xFFEF4444)))
        }

        Spacer(modifier = Modifier.height(24.dp))

        DecisionResult(emoji = "🎰", value = "Pizza")

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text("Go", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Purple
            )
        ) {
            Text("Share Result", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

// One reel of the slot machine: a white box with an emoji and a name.
@Composable
private fun SlotReel(emoji: String, name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(14.dp))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 30.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Text(name, color = Ink, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

// The history screen. It is empty for now: the saved decisions come later.
@Composable
private fun HistoryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("‹", modifier = Modifier.padding(horizontal = 10.dp), color = Purple, fontSize = 38.sp)
            Text(
                text = "History",
                modifier = Modifier.weight(1f),
                color = Ink,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            // The button that will delete the whole history.
            Text("🗑", modifier = Modifier.padding(horizontal = 10.dp), fontSize = 20.sp)
        }

        // There is nothing to show yet, so the rest of the screen is empty.
        Spacer(modifier = Modifier.weight(1f))
    }
}

// The big dice on the start screen.
@Composable
private fun DiceIllustration() {
    Box(
        modifier = Modifier
            .size(230.dp)
            .background(Color(0xFFEFE8FF), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .background(Color.White, RoundedCornerShape(28.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("🎲", fontSize = 76.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StartScreenPreview() {
    MaterialTheme {
        StartScreen(onStart = { }, onHistory = { })
    }
}

@Preview(showBackground = true)
@Composable
private fun OptionSelectionScreenPreview() {
    MaterialTheme {
        OptionSelectionScreen(
            selectedOptions = listOf("🍕 Pizza"),
            customOptions = listOf("Watch a film"),
            customText = "",
            onToggle = { },
            onCustomTextChange = { },
            onAddCustom = { },
            onNext = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CoinScreenPreview() {
    MaterialTheme {
        CoinScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun DecisionScreenPreview() {
    MaterialTheme {
        DecisionScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun WheelScreenPreview() {
    MaterialTheme {
        WheelScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun SlotScreenPreview() {
    MaterialTheme {
        SlotScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryScreenPreview() {
    MaterialTheme {
        HistoryScreen()
    }
}
