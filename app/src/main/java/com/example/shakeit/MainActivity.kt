package com.example.shakeit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.shakeit.data.HistoryStore
import com.example.shakeit.ui.screens.HistoryScreen
import com.example.shakeit.ui.screens.MethodChoiceScreen
import com.example.shakeit.ui.screens.OptionSelectionScreen
import com.example.shakeit.ui.screens.StartScreen
import com.example.shakeit.ui.screens.toggleOption
import com.example.shakeit.ui.theme.Page
import com.example.shakeit.ui.theme.ShakeItTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ShakeItTheme {
                val context = LocalContext.current
                var screen by remember { mutableStateOf("start") }   // which screen we are on
                var selectedOptions by remember { mutableStateOf(listOf<String>()) }  // what the user picked
                var customOptions by remember { mutableStateOf(listOf<String>()) }
                var customText by remember { mutableStateOf("") }
                var method by remember { mutableStateOf("") }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Page
                ) {
                    Scaffold(containerColor = Page) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            when (screen) {
                                "options" -> OptionSelectionScreen(
                                    selectedOptions = selectedOptions,
                                    customOptions = customOptions,
                                    customText = customText,
                                    onToggle = { option ->
                                        selectedOptions = toggleOption(selectedOptions, option)
                                    },
                                    onCustomTextChange = { customText = it },
                                    onAddCustom = {
                                        if (customText.isNotBlank()) {
                                            customOptions = customOptions + customText.trim()
                                            customText = ""
                                        }
                                    },
                                    onBack = { screen = "start" },
                                    onNext = {
                                        if (selectedOptions.size == 2) {
                                            openDecision(context, selectedOptions, 0)
                                        } else if (selectedOptions.size == 6) {
                                            openDecision(context, selectedOptions, 1)
                                        } else {
                                            screen = "method"
                                        }
                                    }
                                )

                                "method" -> MethodChoiceScreen(
                                    optionCount = selectedOptions.size,
                                    selectedMethod = method,
                                    onSelect = { method = it },
                                    onBack = { screen = "options" },
                                    onStart = {
                                        if (method == "Wheel") {
                                            openDecision(context, selectedOptions, 2)
                                        } else if (method == "Slot") {
                                            openDecision(context, selectedOptions, 3)
                                        }
                                    }
                                )

                                "history" -> HistoryScreen(
                                    decisions = HistoryStore.items.value,
                                    onBack = { screen = "start" },
                                    onClear = { HistoryStore.clear() }
                                )

                                else -> StartScreen(
                                    onStart = { screen = "options" },
                                    onHistory = { screen = "history" }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


// open the decision screen with the options
private fun openDecision(context: Context, options: List<String>, startPage: Int) {
    val intent = Intent(context, DecisionActivity::class.java)
    intent.putStringArrayListExtra(DecisionActivity.EXTRA_OPTIONS, ArrayList(options))
    intent.putExtra(DecisionActivity.EXTRA_START_PAGE, startPage)
    context.startActivity(intent)
}
