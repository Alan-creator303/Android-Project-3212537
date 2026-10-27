package com.example.shakeit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Purple = Color(0xFF8B5CF6)
private val Ink = Color(0xFF211A2E)
private val Muted = Color(0xFF777183)
private val Page = Color(0xFFF8F6FC)

private data class DecisionOption(
    val name: String,
    val emoji: String
)

private data class OptionCategory(
    val name: String,
    val options: List<DecisionOption>
)

private val optionCategories = listOf(
    OptionCategory("Sports", listOf(
        DecisionOption("Running", "🏃"),
        DecisionOption("Basketball", "🏀"),
        DecisionOption("Soccer", "⚽"),
        DecisionOption("Fitness", "🏋️"),
        DecisionOption("Swimming", "🏊"),
        DecisionOption("Badminton", "🏸"),
        DecisionOption("Cycling", "🚴"),
        DecisionOption("Tennis", "🎾"),
        DecisionOption("Table Tennis", "🏓")
    )),
    OptionCategory("Food", listOf(
        DecisionOption("Burger", "🍔"),
        DecisionOption("Fries", "🍟"),
        DecisionOption("Steak", "🥩"),
        DecisionOption("Cola", "🥤"),
        DecisionOption("Sandwich", "🥪"),
        DecisionOption("Pizza", "🍕"),
        DecisionOption("Noodles", "🍜"),
        DecisionOption("Juice", "🧃")
    )),
    OptionCategory("Daily Tasks", listOf(
        DecisionOption("Homework", "📚"),
        DecisionOption("Reading", "📖"),
        DecisionOption("Party", "🎉"),
        DecisionOption("Rest", "🛌")
    ))
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val pageState = rememberPagerState(pageCount = { 2 })

                Surface(modifier = Modifier.fillMaxSize(), color = Page) {
                    HorizontalPager(state = pageState) { page ->
                        if (page == 0) StartScreen() else OptionSelectionScreen()
                    }
                }
            }
        }
    }
}

@Composable
private fun StartScreen() {
    Column(
        modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()
            .padding(horizontal = 28.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Shake It!", color = Ink, fontSize = 38.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(8.dp))
        Text("Help you make choices.", color = Muted, fontSize = 16.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(40.dp))
        DiceIllustration()
        Spacer(Modifier.height(48.dp))
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth().height(58.dp).shadow(10.dp, RoundedCornerShape(18.dp)),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text("Start Your Decision", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(14.dp))
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Purple)
        ) {
            Text("View History", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(20.dp))
        Text("Page 1 of 2 · Swipe left to view options", color = Muted, fontSize = 12.sp)
    }
}

@Composable
private fun OptionSelectionScreen() {
    Column(modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("‹", modifier = Modifier.padding(horizontal = 10.dp), color = Purple, fontSize = 38.sp)
            Text(
                "Select Options",
                modifier = Modifier.weight(1f),
                color = Ink,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.width(42.dp))
        }

        Text(
            "Choose at least 2 options",
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
            color = Muted,
            fontSize = 14.sp
        )

        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(optionCategories) { category ->
                OptionCategoryRow(category, Modifier.padding(horizontal = 18.dp))
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Custom", color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(8.dp))
                        Text("add your own", color = Purple, fontSize = 12.sp)
                    }
                    Spacer(Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = "",
                            onValueChange = { },
                            modifier = Modifier.weight(1f),
                            enabled = false,
                            singleLine = true,
                            label = { Text("Add your own option...") },
                            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                            shape = RoundedCornerShape(14.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = { },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Purple)
                        ) {
                            Text("Add")
                        }
                    }
                }
            }
        }

        Surface(color = Color.White, shadowElevation = 8.dp) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Selected: 0", color = Ink, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple,
                        disabledContainerColor = Color(0xFFD8D1E5)
                    )
                ) {
                    Text("Next", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(8.dp))
                Text("Page 2 of 2 · Swipe right to return", color = Muted, fontSize = 12.sp, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun OptionCategoryRow(
    category: OptionCategory,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(category.name, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Text("swipe ›", color = Muted, fontSize = 12.sp)
        }
        Spacer(Modifier.height(10.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(category.options) { option ->
                OptionChip(option)
            }
        }
    }
}

@Composable
private fun OptionChip(option: DecisionOption) {
    Row(
        modifier = Modifier.border(1.dp, Color(0xFFE5E1EB), RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(option.emoji, fontSize = 18.sp)
        Spacer(Modifier.width(6.dp))
        Text(option.name, color = Ink, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun DiceIllustration() {
    Box(modifier = Modifier.size(230.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(230.dp)) {
            drawCircle(color = Color(0xFFEFE8FF), radius = size.minDimension * 0.48f)
            drawCircle(color = Color(0xFFDCD0FF), radius = size.minDimension * 0.36f)
        }
        Box(
            modifier = Modifier.size(136.dp).shadow(16.dp, RoundedCornerShape(28.dp))
                .background(Color.White, RoundedCornerShape(28.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                val radius = 8.dp.toPx()
                val left = size.width * 0.22f
                val middle = size.width * 0.50f
                val right = size.width * 0.78f
                val top = size.height * 0.22f
                val center = size.height * 0.50f
                val bottom = size.height * 0.78f
                listOf(
                    Offset(left, top), Offset(right, top), Offset(middle, center),
                    Offset(left, bottom), Offset(right, bottom)
                ).forEach { drawPip(it, radius) }
            }
        }
    }
}

private fun DrawScope.drawPip(center: Offset, radius: Float) {
    drawCircle(color = Purple, radius = radius, center = center)
}

@Preview(showBackground = true)
@Composable
private fun StartScreenPreview() {
    MaterialTheme { StartScreen() }
}

@Preview(showBackground = true)
@Composable
private fun OptionSelectionScreenPreview() {
    MaterialTheme { OptionSelectionScreen() }
}
