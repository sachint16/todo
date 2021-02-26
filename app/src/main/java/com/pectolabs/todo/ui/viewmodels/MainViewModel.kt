package com.pectolabs.todo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pectolabs.todo.db.Task
import com.pectolabs.todo.repositories.MainRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {



    fun insertTask(task: Task) = viewModelScope.launch {
       val isAdded =  mainRepository.insertTask(task)
        Timber.d("IS_ADDED: $isAdded")
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        val isUpdated = mainRepository.updateTask(task)
        Timber.d("IS_UPDATED: $isUpdated")
    }
}