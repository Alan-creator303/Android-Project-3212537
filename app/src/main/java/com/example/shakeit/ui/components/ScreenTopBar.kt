package com.example.shakeit.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shakeit.ui.theme.Ink
import com.example.shakeit.ui.theme.Purple

@Composable
fun ScreenTopBar(title: String, onBack: () -> Unit, onClear: (() -> Unit)? = null) {   //back button
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

        if (onClear != null) {
            Text(
                text = "🗑",
                modifier = Modifier
                    .clickable { onClear() }
                    .padding(horizontal = 10.dp),
                fontSize = 20.sp
            )
        } else {
            Spacer(modifier = Modifier.width(42.dp))
        }
    }
}
