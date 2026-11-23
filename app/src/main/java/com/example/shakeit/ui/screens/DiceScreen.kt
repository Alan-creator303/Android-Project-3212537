package com.example.shakeit.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shakeit.ui.components.DecisionResult
import com.example.shakeit.ui.components.OptionChips
import com.example.shakeit.ui.components.ShareButton
import com.example.shakeit.ui.components.TopBar
import com.example.shakeit.ui.theme.Muted
import com.example.shakeit.ui.theme.Page
import com.example.shakeit.ui.theme.Purple
import com.example.shakeit.ui.theme.ShakeItTheme

@Composable
fun DiceScreen(
    options: List<String>,
    result: String,
    onBack: () -> Unit,
    onPick: () -> Unit,
    onShare: () -> Unit
) {
    // the dice turns around when a new result comes in
    val diceAngle = remember { Animatable(0f) }

    LaunchedEffect(result) {
        if (result.isNotEmpty()) {
            diceAngle.animateTo(diceAngle.value + 360f, animationSpec = tween(700))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar("Dice Roll", onBack)

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color.White, RoundedCornerShape(28.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("🎲", modifier = Modifier.rotate(diceAngle.value), fontSize = 64.sp)
        }

        Spacer(modifier = Modifier.height(26.dp))

        OptionChips(options)

        Spacer(modifier = Modifier.height(22.dp))

        DecisionResult(emoji = "🎲", value = result)

        Spacer(modifier = Modifier.weight(1f))

        if (result.isEmpty()) {
            Text("or shake your phone", color = Muted, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = onPick,
            enabled = result.isEmpty(),
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
fun DiceScreenPreview() {
    ShakeItTheme {
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
