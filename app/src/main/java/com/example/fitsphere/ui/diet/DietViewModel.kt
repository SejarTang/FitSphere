package com.example.myapplication.ui.diet

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.database.entity.DietEntity
import com.example.myapplication.data.repository.DietRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for managing diet input and history, without Hilt.
 */
class DietViewModel(
    private val repository: DietRepository
) : ViewModel() {

    // UI state for user input
    var selectedFood = mutableStateOf("Select food")
    var calories = mutableStateOf(300f)
    var breakfast = mutableStateOf(false)
    var lunch = mutableStateOf(false)
    var dinner = mutableStateOf(false)

    // List to hold diet history records for display
    val dietHistory = mutableStateListOf<DietEntity>()

    /**
     * Save the current diet input to the database
     */
    fun saveDiet() {
        val entity = DietEntity(
            foodName = selectedFood.value,
            calories = calories.value.toInt(),
            isBreakfast = breakfast.value,
            isLunch = lunch.value,
            isDinner = dinner.value
        )
        viewModelScope.launch {
            repository.saveDietRecord(entity)
            loadHistory() // Refresh history after saving
        }
    }

    /**
     * Load diet records from the database into dietHistory list
     */
    fun loadHistory() {
        viewModelScope.launch {
            val result = repository.getDietHistory()
            dietHistory.clear()
            dietHistory.addAll(result)
        }
    }
}
