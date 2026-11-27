package com.example.shakeit.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shakeit.ui.components.ScreenTopBar
import com.example.shakeit.ui.theme.DisabledPurple
import com.example.shakeit.ui.theme.Ink
import com.example.shakeit.ui.theme.Line
import com.example.shakeit.ui.theme.Muted
import com.example.shakeit.ui.theme.Purple
import com.example.shakeit.ui.theme.ShakeItTheme
import com.example.shakeit.ui.theme.SoftPurple

@Composable
fun MethodChoiceScreen(
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
                disabledContainerColor = DisabledPurple
            )
        ) {
            Text("Start", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MethodCard(  // one card, the wheel or the slot
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
fun MethodChoiceScreenPreview() {
    ShakeItTheme {
        MethodChoiceScreen(
            optionCount = 3,
            selectedMethod = "Wheel",
            onSelect = { },
            onBack = { },
            onStart = { }
        )
    }
}
