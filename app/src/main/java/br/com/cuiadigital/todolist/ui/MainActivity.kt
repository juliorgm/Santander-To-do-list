package br.com.cuiadigital.todolist.ui

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.cuiadigital.todolist.databinding.ActivityMainBinding
import br.com.cuiadigital.todolist.datasource.TaskDataSource
import br.com.cuiadigital.todolist.ui.adapter.TaskAdapter


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.setHasFixedSize(true)
        binding.rvTasks.adapter = adapter
        updateList()

        insertListerner()
    }

    private fun insertListerner() {
        binding.fabNewTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }

        adapter.listenerEdit = { task ->
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, task)
            startActivityForResult(intent, EDIT_TASK)
        }
        adapter.listenerDelete = {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(R.string.dialog_alert_title)
                .setPositiveButton(R.string.yes,
                    DialogInterface.OnClickListener { dialog, id ->
                        TaskDataSource.delete(it)
                        adapter.updateList(TaskDataSource.getList())
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        return@OnClickListener
                    })
            builder.create().show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) updateList()
        if(requestCode == EDIT_TASK && resultCode == Activity.RESULT_OK) updateList()
    }

    private fun updateList() {
        val list = TaskDataSource.getList()

        binding.layoutEmptyState.emptyState.visibility = if(list.isEmpty()) View.VISIBLE else View.GONE

        adapter.updateList(list)
    }

    companion object{
        private const val CREATE_NEW_TASK = 1000
        private const val EDIT_TASK = 3000
    }

}

