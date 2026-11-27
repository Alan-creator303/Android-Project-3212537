package com.example.shakeit.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shakeit.ui.components.DecisionResult
import com.example.shakeit.ui.components.ShareButton
import com.example.shakeit.ui.components.SlotReel
import com.example.shakeit.ui.components.TopBar
import com.example.shakeit.ui.theme.Purple
import com.example.shakeit.ui.theme.SlotBlack
import com.example.shakeit.ui.theme.SlotRed
import com.example.shakeit.ui.theme.Muted

@Composable
fun SlotScreen(
    options: List<String>,
    result: String,
    onBack: () -> Unit,
    onPick: () -> Unit,
    onShare: () -> Unit
) {
    // --- reel animation ---
    // the reels come down one by one
    val reelOne = remember { Animatable(0f) }
    val reelTwo = remember { Animatable(0f) }
    val reelThree = remember { Animatable(0f) }

    LaunchedEffect(result) {
        if (result.isNotEmpty()) {
            reelOne.snapTo(-60f)
            reelTwo.snapTo(-60f)
            reelThree.snapTo(-60f)

            reelOne.animateTo(0f, animationSpec = tween(450))
            reelTwo.animateTo(0f, animationSpec = tween(450))
            reelThree.animateTo(0f, animationSpec = tween(450))
        }
    }

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
                .clip(RoundedCornerShape(22.dp))
                .background(SlotBlack, RoundedCornerShape(22.dp))
                .padding(16.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(3.dp).background(SlotRed))
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                val first = options.take(3)[0]
                val second = options.take(3)[1]
                val third = options.take(3)[2]

                SlotReel(
                    text = if (result.isEmpty()) first else result,
                    offsetY = reelOne.value,
                    modifier = Modifier.weight(1f)
                )
                SlotReel(
                    text = if (result.isEmpty()) second else result,
                    offsetY = reelTwo.value,
                    modifier = Modifier.weight(1f)
                )
                SlotReel(
                    text = if (result.isEmpty()) third else result,
                    offsetY = reelThree.value,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier.fillMaxWidth().height(3.dp).background(SlotRed))
        }

        Spacer(modifier = Modifier.height(24.dp))

        DecisionResult(emoji = "🎰", value = result)

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
            Text("Go", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(10.dp))
        ShareButton(onShare = onShare, enabled = result.isNotEmpty())
    }
}
