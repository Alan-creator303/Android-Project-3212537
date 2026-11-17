package com.example.shakeit.ui.screens

import androidx.compose.foundation.layout.Column
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

@Composable
fun WheelScreen(
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
