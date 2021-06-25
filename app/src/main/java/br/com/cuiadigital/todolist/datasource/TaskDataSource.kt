package br.com.cuiadigital.todolist.datasource

import br.com.cuiadigital.todolist.model.Task

object TaskDataSource {
    private val list = mutableListOf<Task>(
//        Task("Café", "12/07/2021", "18:00", 1),
//        Task("Café", "12/07/2021", "18:00", 2)
    )

    fun getList() = list.toList()

    fun insertTask(task: Task){
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