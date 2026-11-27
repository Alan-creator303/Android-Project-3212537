package com.example.shakeit.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shakeit.ui.components.DecisionResult
import com.example.shakeit.ui.components.OptionChips
import com.example.shakeit.ui.components.ShareButton
import com.example.shakeit.ui.components.TopBar
import com.example.shakeit.ui.components.WheelDrawing
import com.example.shakeit.ui.theme.Purple
import com.example.shakeit.ui.theme.Muted

@Composable
fun WheelScreen(
    options: List<String>,
    result: String,
    onBack: () -> Unit,
    onPick: () -> Unit,
    onShare: () -> Unit
) {
    // --- spin animation ---
    // the wheel spins when we get a result
    val wheelAngle = remember { Animatable(0f) }

    // --- loading spinner ---
    // true while the wheel is turning, so we can show the loading spinner
    var isSpinning by remember { mutableStateOf(false) }

    LaunchedEffect(result) {
        if (result.isNotEmpty()) {
            val index = options.indexOf(result)
            if (index >= 0) {
                isSpinning = true

                val slice = 360f / 6f
                // the pointer is at the top, that is 270 degrees
                // so turn the wheel until the middle of our slice is there
                val middleOfSlice = (index % 6) * slice + slice / 2f
                val target = 360f * 3f + (270f - middleOfSlice)
                wheelAngle.animateTo(target, animationSpec = tween(1600))

                isSpinning = false
            }
        }
    }

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

        Text("▼", color = Purple, fontSize = 18.sp)

        WheelDrawing(rotation = wheelAngle.value)

        Spacer(modifier = Modifier.height(16.dp))

        // while the wheel is turning, show a small loading spinner
        Box(modifier = Modifier.height(26.dp), contentAlignment = Alignment.Center) {
            if (isSpinning) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Purple,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Deciding...", color = Muted, fontSize = 13.sp)
                }
            }
        }

        DecisionResult(emoji = "🎡", value = result)

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
            Text("Spin", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(10.dp))
        ShareButton(onShare = onShare, enabled = result.isNotEmpty())
    }
}
