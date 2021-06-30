package br.com.cuiadigital.todolist.ui.list_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.cuiadigital.todolist.datasource.TaskDataSource
import br.com.cuiadigital.todolist.model.Task

class ListTaskViewModel: ViewModel(){
    private var _taskList = MutableLiveData<List<Task>>()
    val tasklist: LiveData<List<Task>>
        get() = _taskList

    init {
        updateTaskList()
    }

    fun updateTaskList(){
        _taskList.value = TaskDataSource.getList()
    }

    fun deleteTask(it: Task) {
        TaskDataSource.delete(it)
        updateTaskList()
    }
}