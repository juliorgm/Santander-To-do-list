package br.com.cuiadigital.todolist.data

import androidx.annotation.WorkerThread
import br.com.cuiadigital.todolist.model.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: TaskDao) {
    fun getAllTasks(): Flow<List<Task>> = dao.getAll()

    @WorkerThread
    suspend fun insert(task: Task){
        dao.insert(task)
    }

    @WorkerThread
    suspend fun update(task: Task){
        dao.update(task)
    }

    @WorkerThread
    suspend fun delete(task: Task){
        dao.delete(task)
    }
}