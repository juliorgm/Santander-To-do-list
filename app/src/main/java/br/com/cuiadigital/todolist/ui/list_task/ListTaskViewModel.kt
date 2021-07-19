package br.com.cuiadigital.todolist.ui.list_task

import androidx.lifecycle.*
import br.com.cuiadigital.todolist.data.TaskRepository
import br.com.cuiadigital.todolist.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ListTaskViewModel(private val repository: TaskRepository) : ViewModel() {

    fun getAllTasks(): Flow<List<Task>> = repository.getAllTasks()

    fun delete(task: Task) {
        viewModelScope.launch { repository.delete(task) }
    }
}

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListTaskViewModel::class.java)) {
            @Suppress("UNCHECKED_LIST")
            return ListTaskViewModel(repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}