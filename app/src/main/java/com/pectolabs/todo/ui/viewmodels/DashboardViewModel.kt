package com.pectolabs.todo.ui.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pectolabs.todo.db.Task
import com.pectolabs.todo.repositories.MainRepository
import com.pectolabs.todo.utils.DashboardFilterType
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class DashboardViewModel @Inject constructor(val mainRepository: MainRepository) :
    ViewModel() {

    private val allTasks = mainRepository.getAllTasks()
    private val allPendingTasks = mainRepository.getAllPendingTasks()
    private val allCompletedTasks = mainRepository.getAllCompletedTasks()


    val tasks = MediatorLiveData<List<Task>>()
    var dashboardFilterType = DashboardFilterType.PENDING

    init {
        tasks.addSource(allPendingTasks) { result ->
            if (dashboardFilterType == DashboardFilterType.PENDING) {
                result?.let {
                    tasks.value = it
                }
            }

        }

        tasks.addSource(allCompletedTasks) { result ->
            if (dashboardFilterType == DashboardFilterType.COMPLETED) {
                result?.let {
                    tasks.value = it
                }
            }

        }

        tasks.addSource(allTasks) { result ->
            if (dashboardFilterType == DashboardFilterType.ALL_TASK) {
                result?.let {
                    tasks.value = it
                }
            }

        }
    }

    fun filterTasks(dashboardFilterType: DashboardFilterType) = when (dashboardFilterType) {
        DashboardFilterType.PENDING -> allPendingTasks.value?.let {
            tasks.value = it
        }

        DashboardFilterType.COMPLETED -> allCompletedTasks.value?.let {
            tasks.value = it
        }

        DashboardFilterType.ALL_TASK -> allTasks.value?.let {
            tasks.value = it
        }

    }.also {
        this.dashboardFilterType = dashboardFilterType
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        val isAdded =  mainRepository.deleteTask(task)
        Timber.d("IS_DELETED: $isAdded")
    }
}