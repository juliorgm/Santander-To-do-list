package br.com.cuiadigital.todolist.data

import androidx.room.*
import br.com.cuiadigital.todolist.model.Task
import kotlinx.coroutines.flow.Flow


@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAll(): Flow<List<Task>>

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}