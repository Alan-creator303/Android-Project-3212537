package com.example.shakeit.data

import androidx.compose.runtime.mutableStateOf

// the decisions are kept in memory for now, the database comes in milestone 3
object HistoryStore {

    var items = mutableStateOf(listOf<String>())

    fun add(item: String) {
        items.value = items.value + item
    }

    fun clear() {
        items.value = emptyList()
    }
}
