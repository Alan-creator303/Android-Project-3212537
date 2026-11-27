package com.example.shakeit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shakeit.ui.theme.Ink
import com.example.shakeit.ui.theme.Line
import com.example.shakeit.ui.theme.Purple
import com.example.shakeit.ui.theme.SoftPurple

@Composable
fun OptionChip(option: String, isSelected: Boolean, onClick: () -> Unit) {  // the chip turns purple when it is picked
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
