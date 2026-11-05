package com.example.shakeit

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.platform.LocalContext
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
                val pagerState = rememberPagerState(pageCount = { 3 })
                val scope = rememberCoroutineScope()
                val context = LocalContext.current

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
                                    onHistory = { goToPage(2) }
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
                                    onNext = {
                                        // Explicit intent: we name the activity we want to open,
                                        // and we send the selected options inside the intent.
                                        val intent = Intent(context, DecisionActivity::class.java)
                                        intent.putStringArrayListExtra(
                                            DecisionActivity.EXTRA_OPTIONS,
                                            ArrayList(selectedOptions)
                                        )
                                        context.startActivity(intent)
                                    }
                                )

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
