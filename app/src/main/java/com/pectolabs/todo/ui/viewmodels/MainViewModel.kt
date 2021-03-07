package com.pectolabs.todo.ui.viewmodels

import androidx.lifecycle.*
import com.pectolabs.todo.db.Task
import com.pectolabs.todo.repositories.MainRepository
import com.pectolabs.todo.utils.DashboardFilterType
import com.pectolabs.todo.utils.DateUtils
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val allTasks = mainRepository.getAllTasks()
    private val allPendingTasks = mainRepository.getAllPendingTasks()
    private val allCompletedTasks = mainRepository.getAllCompletedTasks()
    private val tasksFilteredByDate = mainRepository.getAllPendingTasks()

    val tasks = MediatorLiveData<List<Task>>()
    val calendarTasks = MediatorLiveData<List<Task>>()
    val dashboardFilterType = MutableLiveData(DashboardFilterType.PENDING)

    init {
        tasks.addSource(allPendingTasks) { result ->
            if (dashboardFilterType.value == DashboardFilterType.PENDING) {
                result?.let {
                    tasks.value = it
                }
            }

        }

        tasks.addSource(allCompletedTasks) { result ->
            if (dashboardFilterType.value == DashboardFilterType.COMPLETED) {
                result?.let {
                    tasks.value = it
                }
            }

        }

        tasks.addSource(allTasks) { result ->
            if (dashboardFilterType.value == DashboardFilterType.ALL_TASK) {
                result?.let {
                    tasks.value = it
                }
            }

        }

        calendarTasks.addSource(tasksFilteredByDate) { result ->
            result?.filter {
                val scheduledAt = DateUtils.fromDateObjectToString(it.scheduledAt)
                val selectedDate = DateUtils.fromDateObjectToString(Calendar.getInstance().time)

                selectedDate == scheduledAt
            }.let {
                if (it.isNullOrEmpty()){
                    calendarTasks.value = emptyList()
                }else{
                    calendarTasks.value = it
                }
            }

        }
    }

    fun filterTasks(filterType: DashboardFilterType) = when (filterType) {
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
        this.dashboardFilterType.value = filterType

        Timber.d("FILTER_TYPE:${this.dashboardFilterType}")
    }

    fun filterTasksWithDate(date: Date) {
        tasksFilteredByDate.value?.filter {
            val scheduledAt = DateUtils.fromDateObjectToString(it.scheduledAt)
            val selectedDate = DateUtils.fromDateObjectToString(date)

            selectedDate == scheduledAt
        }.let {
            if (it.isNullOrEmpty()){
                calendarTasks.value = emptyList()
            }else{
                calendarTasks.value = it
            }
        }
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        mainRepository.deleteTask(task)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        val isUpdated = mainRepository.updateTask(task)
        Timber.d("IS_UPDATED: $isUpdated")
    }


    fun insertTask(task: Task) = viewModelScope.launch {
        val isAdded = mainRepository.insertTask(task)
        Timber.d("IS_ADDED: $isAdded")
    }
}