package br.com.cuiadigital.todolist.ui.list_task

import androidx.lifecycle.*
import br.com.cuiadigital.todolist.data.TaskRepository
import br.com.cuiadigital.todolist.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository): ViewModel(){

    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task>
        get() = _task

    val isEditable = MutableLiveData<Boolean>()


    init {
        loadValues()
    }

    private fun loadValues() {
        _task.value = Task()
        isEditable.value = false
    }

    fun getAllTasks(): Flow<List<Task>> = repository.getAllTasks()

    fun insert() {
        viewModelScope.launch { task.value?.let { repository.insert(it) } }
    }

    fun update() {
        viewModelScope.launch { task.value?.let { repository.update(it) } }
        loadValues()
    }

    fun delete(task: Task) {
        viewModelScope.launch { repository.delete(task) }
    }

    fun updateTask(title: String, date: String, hour: String){
        _task.value?.title = title
        _task.value?.date = date
        _task.value?.hour = hour
    }

    fun updateTask(task: Task){
        _task.value = task
    }

}


class TaskViewModelFactory(private val repository: TaskRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)){
            @Suppress("UNCHECKED_LIST")
            return TaskViewModel(repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}