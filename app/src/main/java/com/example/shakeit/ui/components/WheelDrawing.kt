package com.example.shakeit.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shakeit.ui.theme.Purple
import com.example.shakeit.ui.theme.WheelAmber
import com.example.shakeit.ui.theme.WheelBlue
import com.example.shakeit.ui.theme.WheelGreen
import com.example.shakeit.ui.theme.WheelPink
import com.example.shakeit.ui.theme.WheelPurple
import com.example.shakeit.ui.theme.WheelRose

@Composable
fun WheelDrawing() {   //draws the wheel with six slices
    val sliceColors = listOf(
        WheelPurple,
        WheelPink,
        WheelGreen,
        WheelAmber,
        WheelBlue,
        WheelRose
    )

    Canvas(modifier = Modifier.size(210.dp)) {
        val slice = 360f / sliceColors.size
        sliceColors.forEachIndexed { index, color ->
            drawArc(
                color = color,
                startAngle = index * slice,
                sweepAngle = slice,
                useCenter = true
            )
        }

        drawCircle(color = Purple, radius = size.minDimension / 8f)
        drawCircle(color = Color.White, radius = size.minDimension / 11f)
    }
}
