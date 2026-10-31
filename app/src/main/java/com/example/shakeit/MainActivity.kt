package com.example.shakeit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val pagerState = rememberPagerState(pageCount = { 3 })

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Page
                ) {
                    // Swipe left or right to look at the three screen layouts.
                    HorizontalPager(state = pagerState) { page ->
                        when (page) {
                            0 -> StartScreen()
                            1 -> OptionSelectionScreen()
                            else -> DecisionScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StartScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 28.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Shake It!",
            color = Ink,
            fontSize = 38.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Help you make choices.",
            color = Muted,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))
        DiceIllustration()
        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .shadow(10.dp, RoundedCornerShape(18.dp)),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text("Start Your Decision", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(14.dp))
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Purple
            )
        ) {
            Text("View History", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun OptionSelectionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("‹", modifier = Modifier.padding(horizontal = 10.dp), color = Purple, fontSize = 38.sp)
            Text(
                text = "Select Options",
                modifier = Modifier.weight(1f),
                color = Ink,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(42.dp))
        }

        Text(
            text = "Choose at least 2 options",
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
            color = Muted,
            fontSize = 14.sp
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OptionCategory("Sports", listOf(
                    "🏃 Running", "🏀 Basketball", "⚽ Soccer", "🏋️ Fitness",
                    "🏊 Swimming", "🏸 Badminton", "🚴 Cycling", "🎾 Tennis", "🏓 Table Tennis"
                ))
            }
            item {
                OptionCategory("Food", listOf(
                    "🍔 Burger", "🍟 Fries", "🥩 Steak", "🥤 Cola",
                    "🥪 Sandwich", "🍕 Pizza", "🍜 Noodles", "🧃 Juice"
                ))
            }
            item {
                OptionCategory("Daily Tasks", listOf(
                    "📚 Homework", "📖 Reading", "🎉 Party", "🛌 Rest"
                ))
            }
            item {
                CustomOptionSection()
            }
        }

        Surface(color = Color.White, shadowElevation = 8.dp) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Selected: 0",
                    modifier = Modifier.padding(bottom = 10.dp),
                    color = Ink,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
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
            }
        }
    }
}

@Composable
private fun OptionCategory(title: String, options: List<String>) {
    Column(modifier = Modifier.padding(horizontal = 18.dp)) {
        Text(title, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(options) { option ->
                OptionChip(option)
            }
        }
    }
}

@Composable
private fun CustomOptionSection() {
    Column(modifier = Modifier.padding(horizontal = 18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Custom", color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Text("add your own idea", color = Purple, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = "",
                onValueChange = { },
                modifier = Modifier.weight(1f),
                enabled = false,
                singleLine = true,
                label = { Text("Add your own option...") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                shape = RoundedCornerShape(14.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
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

@Composable
private fun OptionChip(option: String) {
    Text(
        text = option,
        modifier = Modifier
            .border(1.dp, Color(0xFFE5E1EB), RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        color = Ink,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
    )
}


@Composable
private fun DecisionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 22.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("‹", modifier = Modifier.padding(horizontal = 6.dp), color = Purple, fontSize = 34.sp)
            Text(
                text = "Decision",
                modifier = Modifier.weight(1f),
                color = Ink,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(28.dp))
        }


        Spacer(modifier = Modifier.height(24.dp))

        // The dice in the middle of the screen.
        Box(
            modifier = Modifier
                .size(120.dp)
                .shadow(12.dp, RoundedCornerShape(28.dp))
                .background(Color.White, RoundedCornerShape(28.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("🎲", fontSize = 64.sp)
        }

        Spacer(modifier = Modifier.height(26.dp))

        // The options the user picked, three small cards in one row.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OptionCard("🍕", "Pizza", Modifier.weight(1f))
            OptionCard("🍔", "Burger", Modifier.weight(1f))
            OptionCard("🥩", "Steak", Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(22.dp))

        // The purple box that will show the result.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Purple, RoundedCornerShape(22.dp))
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎲 RESULT",
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Pizza",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text("Roll", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Purple
            )
        ) {
            Text("Share Result", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun OptionCard(emoji: String, name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .border(1.dp, Color(0xFFE5E1EB), RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Text(name, color = Ink, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun DiceIllustration() {
    Box(
        modifier = Modifier
            .size(230.dp)
            .background(Color(0xFFEFE8FF), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .shadow(12.dp, RoundedCornerShape(28.dp))
                .background(Color.White, RoundedCornerShape(28.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("🎲", fontSize = 76.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StartScreenPreview() {
    MaterialTheme {
        StartScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun OptionSelectionScreenPreview() {
    MaterialTheme {
        OptionSelectionScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun DecisionScreenPreview() {
    MaterialTheme {
        DecisionScreen()
    }
}
