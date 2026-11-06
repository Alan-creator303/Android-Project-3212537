package com.example.shakeit

import android.content.Context
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
private val SoftPurple = Color(0xFFF3EEFE)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val context = LocalContext.current
                var screen by remember { mutableStateOf("start") }    //which screen is showing right now
                var selectedOptions by remember { mutableStateOf(listOf<String>()) }  //show what the user picked and typed
                var customOptions by remember { mutableStateOf(listOf<String>()) }
                var customText by remember { mutableStateOf("") }
                var method by remember { mutableStateOf("") }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Page
                ) {
                    Scaffold(containerColor = Page) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            when (screen) {
                                "options" -> OptionSelectionScreen(
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
                                    onBack = { screen = "start" },
                                    onNext = {
                                        when (selectedOptions.size) {
                                            2 -> openDecision(context, selectedOptions, 0)
                                            6 -> openDecision(context, selectedOptions, 1)
                                            else -> screen = "method"
                                        }
                                    }
                                )

                                "method" -> MethodChoiceScreen(
                                    optionCount = selectedOptions.size,
                                    selectedMethod = method,
                                    onSelect = { method = it },
                                    onBack = { screen = "options" },
                                    onStart = {
                                        if (method == "Wheel") {
                                            openDecision(context, selectedOptions, 2)
                                        } else if (method == "Slot") {
                                            openDecision(context, selectedOptions, 3)
                                        }
                                    }
                                )

                                "history" -> HistoryScreen(onBack = { screen = "start" })

                                else -> StartScreen(
                                    onStart = { screen = "options" },
                                    onHistory = { screen = "history" }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


//open the decision screen and show the options
private fun openDecision(context: Context, options: List<String>, startPage: Int) {
    val intent = Intent(context, DecisionActivity::class.java)
    intent.putStringArrayListExtra(DecisionActivity.EXTRA_OPTIONS, ArrayList(options))
    intent.putExtra(DecisionActivity.EXTRA_START_PAGE, startPage)
    context.startActivity(intent)
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
private fun ScreenTopBar(title: String, onBack: () -> Unit) {   //back button
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "‹",
            modifier = Modifier
                .clickable { onBack() }
                .padding(horizontal = 10.dp),
            color = Purple,
            fontSize = 38.sp
        )
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = Ink,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.width(42.dp))
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
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ScreenTopBar(title = "Select Options", onBack = onBack)


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


                val goesStraight = selectedOptions.size == 2 || selectedOptions.size == 6  // 2 or 6 options go straight to a coin or a dice

                Button(
                    onClick = onNext,
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    enabled = selectedOptions.size >= 2,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple,
                        disabledContainerColor = Color(0xFFD8D1E5)
                    )
                ) {
                    Text(
                        text = if (goesStraight) "Start" else "Next",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
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
        Text("Custom", color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))

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
private fun OptionChip(option: String, isSelected: Boolean, onClick: () -> Unit) {  //detect if the options were selected or not
    Text(
        text = option,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = if (isSelected) Purple else Line,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = if (isSelected) SoftPurple else Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        color = if (isSelected) Purple else Ink,
        fontSize = 13.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
    )
}

private fun toggleOption(options: List<String>, option: String): List<String> {  // adds the option or removes it
    return if (options.contains(option)) {
        options - option
    } else {
        options + option
    }
}

@Composable
private fun MethodChoiceScreen(
    optionCount: Int,
    selectedMethod: String,
    onSelect: (String) -> Unit,
    onBack: () -> Unit,
    onStart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ScreenTopBar(title = "Pick Your Decision Method", onBack = onBack)

        Text(
            text = "You have $optionCount options. Choose how to decide:",
            modifier = Modifier.padding(start = 22.dp, end = 22.dp),
            color = Muted,
            fontSize = 13.sp
        )

        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MethodCard(
                emoji = "🎡",
                title = "Spinner Wheel",
                description = "Spin a colorful wheel - great for 3+ options.",
                isSelected = selectedMethod == "Wheel",
                onClick = { onSelect("Wheel") }
            )
            MethodCard(
                emoji = "🎰",
                title = "Slot Machine",
                description = "Three reels roll and stop on your fate.",
                isSelected = selectedMethod == "Slot",
                onClick = { onSelect("Slot") }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onStart,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
                .height(54.dp),
            enabled = selectedMethod.isNotEmpty(),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple,
                disabledContainerColor = Color(0xFFD8D1E5)
            )
        ) {
            Text("Start", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun MethodCard(  //choose spinner wheel or slot machine
    emoji: String,
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Purple else Line,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                color = if (isSelected) SoftPurple else Color.White,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .background(SoftPurple, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(emoji, fontSize = 30.sp)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, color = Muted, fontSize = 12.sp)
        }
    }
}


@Composable
private fun HistoryScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ScreenTopBar(title = "History", onBack = onBack)

        Spacer(modifier = Modifier.weight(1f))
    }
}


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
            onBack = { },
            onNext = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MethodChoiceScreenPreview() {
    MaterialTheme {
        MethodChoiceScreen(
            optionCount = 3,
            selectedMethod = "Wheel",
            onSelect = { },
            onBack = { },
            onStart = { }
        )
    }
}
