package com.example.shakeit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shakeit.ui.components.DecisionResult
import com.example.shakeit.ui.components.OptionCard
import com.example.shakeit.ui.components.ShareButton
import com.example.shakeit.ui.components.TopBar
import com.example.shakeit.ui.theme.CoinGold
import com.example.shakeit.ui.theme.Ink
import com.example.shakeit.ui.theme.Muted
import com.example.shakeit.ui.theme.Page
import com.example.shakeit.ui.theme.Purple
import com.example.shakeit.ui.theme.ShakeItTheme

@Composable
fun CoinScreen(
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
                .background(CoinGold, CircleShape)
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
            Text("Flip", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(10.dp))
        ShareButton(onShare = onShare, enabled = result.isNotEmpty())
    }
}

@Composable
fun CoinScreenPreview() {
    ShakeItTheme {
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
