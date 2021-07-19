package br.com.cuiadigital.todolist.ui.form_task

import androidx.lifecycle.*
import br.com.cuiadigital.todolist.data.TaskRepository
import br.com.cuiadigital.todolist.model.Task
import kotlinx.coroutines.launch

class FormTaskViewModel(private val repository: TaskRepository): ViewModel(){

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

    fun insert() {
        viewModelScope.launch { task.value?.let { repository.insert(it) } }
    }

    fun update() {
        viewModelScope.launch { task.value?.let { repository.update(it) } }
        loadValues()
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

class FormTaskViewModelFactory(private val repository: TaskRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FormTaskViewModel::class.java)){
            @Suppress("UNCHECKED_LIST")
            return FormTaskViewModel(repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}