package br.com.cuiadigital.todolist

import android.app.Application
import br.com.cuiadigital.todolist.data.TaskDatabase
import br.com.cuiadigital.todolist.data.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TaskApplication : Application() {
    val database by lazy { TaskDatabase.getInstance(this) }
    val repository by lazy { TaskRepository(database.taskDao()) }
}