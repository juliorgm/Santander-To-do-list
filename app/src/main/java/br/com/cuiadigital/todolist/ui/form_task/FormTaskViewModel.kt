package br.com.cuiadigital.todolist.ui.form_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.cuiadigital.todolist.datasource.TaskDataSource
import br.com.cuiadigital.todolist.model.Task

class FormTaskViewModel: ViewModel() {
    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task>
        get() = _task

    init {
        _task.value = Task()
    }

    fun updateTask(title: String, date: String, hour: String){
        _task.value?.title = title
        _task.value?.date = date
        _task.value?.hour = hour
    }

    fun updateTask(task: Task){
        _task.value = task
    }

    fun saveTask(){
        task.value?.let { TaskDataSource.saveTask(it) }
    }
}