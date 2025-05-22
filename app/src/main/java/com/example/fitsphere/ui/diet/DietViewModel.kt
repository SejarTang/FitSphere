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
class DietViewModel(private val repository: DietRepository) : ViewModel() {
    val selectedFood = mutableStateOf("Select food")
    val calories = mutableStateOf(300f)
    val breakfast = mutableStateOf(false)
    val lunch = mutableStateOf(false)
    val dinner = mutableStateOf(false)

    val dietHistory = mutableStateListOf<DietEntity>()
    val showSavedMessage = mutableStateOf(false)

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
            loadHistory()
            showSavedMessage.value = true
        }
    }

    fun deleteDiet(record: DietEntity) {
        viewModelScope.launch {
            repository.deleteDietRecord(record)
            loadHistory()
        }
    }

    fun loadHistory() {
        viewModelScope.launch {
            val result = repository.getDietHistory()
            dietHistory.clear()
            dietHistory.addAll(result)
        }
    }
}

