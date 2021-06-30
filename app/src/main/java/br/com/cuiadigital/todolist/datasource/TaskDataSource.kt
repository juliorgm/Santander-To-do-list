package br.com.cuiadigital.todolist.datasource

import br.com.cuiadigital.todolist.model.Task

object TaskDataSource {
    private val list = mutableListOf(
        Task(title ="Café", date ="12/07/2021", hour = "18:00", id = 1),
        Task(title = "Café", date ="12/07/2021", hour = "18:00", id = 2)
    )

    fun getList() = list.toList()

    fun saveTask(task: Task){
        if (task.id == 0){
            list.add(task.copy(id = list.size + 1))
        }else{
            list.remove(task)
            list.add(task)
        }
    }

    fun delete(task: Task) {
        list.remove(task)
    }
}