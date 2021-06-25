package br.com.cuiadigital.todolist.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import br.com.cuiadigital.todolist.R
import br.com.cuiadigital.todolist.databinding.ActivityAddTaskBinding
import br.com.cuiadigital.todolist.datasource.TaskDataSource
import br.com.cuiadigital.todolist.extensions.format
import br.com.cuiadigital.todolist.extensions.text
import br.com.cuiadigital.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*


class AddTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        task = intent.getParcelableExtra<Task>(TASK_ID) ?:Task()
        if (intent.hasExtra(TASK_ID)){
            task?.let {
                binding.tilTitle.text = it.title
                binding.tilDate.text = it.date
                binding.tilTime.text = it.hour
            }

            binding.btnAddTask.text = getText(R.string.btn_edit_task)
        }

        insertListeners()
    }

    private fun insertListeners() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, DATEPICKER_TAG)

            datePicker.addOnPositiveButtonClickListener {
                val timeZone= TimeZone.getDefault()
                val offset= timeZone.getOffset(Date().time) * -1
                binding.tilDate.text= Date(it + offset).format()
            }
        }
        binding.tilTime.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val minute = if(timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if(timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                binding.tilTime.text = "$hour:$minute"
            }

            timePicker.show(supportFragmentManager, null)
        }

        binding.btnAddTask.setOnClickListener {
            task.title = binding.tilTitle.text
            task.date = binding.tilDate.text
            task.hour = binding.tilTime.text

            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }
        binding.btnCancel.setOnClickListener { finish() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    companion object{
        val TASK_ID = "TASK_ID"
        private val DATEPICKER_TAG = "DATEPICKER_TAG"
        private val TIMEPICKER_TAG = "TIMEPICKER_TAG"
    }
}