package com.example.shakeit.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shakeit.ui.components.OptionChip
import com.example.shakeit.ui.components.ScreenTopBar
import com.example.shakeit.ui.theme.DisabledPurple
import com.example.shakeit.ui.theme.Ink
import com.example.shakeit.ui.theme.Purple
import com.example.shakeit.ui.theme.ShakeItTheme

@Composable
fun OptionSelectionScreen(
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
                        disabledContainerColor = DisabledPurple
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OptionCategory(
    title: String,
    options: List<String>,
    selectedOptions: List<String>,
    onToggle: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 18.dp)) {
        Text(title, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        // the chips wrap onto the next line, so no option is hidden
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                OptionChip(
                    option = option,
                    isSelected = selectedOptions.contains(option),
                    onClick = { onToggle(option) }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomOptionSection(
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

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            customOptions.forEach { option ->
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

fun toggleOption(options: List<String>, option: String): List<String> {  // adds the option or removes it
    return if (options.contains(option)) {
        options - option
    } else {
        options + option
    }
}

@Composable
fun OptionSelectionScreenPreview() {
    ShakeItTheme {
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
