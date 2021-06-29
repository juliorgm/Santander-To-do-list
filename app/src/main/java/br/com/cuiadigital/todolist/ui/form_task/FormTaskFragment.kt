package br.com.cuiadigital.todolist.ui.form_task

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import br.com.cuiadigital.todolist.R
import br.com.cuiadigital.todolist.databinding.FragmentFormTaskBinding
import br.com.cuiadigital.todolist.databinding.FragmentListTaskBinding
import br.com.cuiadigital.todolist.datasource.TaskDataSource
import br.com.cuiadigital.todolist.extensions.format
import br.com.cuiadigital.todolist.extensions.text
import br.com.cuiadigital.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class FormTaskFragment : Fragment() {
    private lateinit var binding: FragmentFormTaskBinding
    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        insertListeners()

        arguments?.let {
            task = it.getParcelable<Task>(TASK_ID) ?: Task()

            if (isEditable(task))   {
                loadTaskInForm(task)
                binding.btnAddTask.text = getString(R.string.btn_edit_task)
            }
        }
    }

    private fun isEditable(task: Task): Boolean {
        return when (task.id){
            NEW_TASK -> return false
            else -> return true
        }
    }

    private fun insertListeners() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            activity?.supportFragmentManager?.let { it1 -> datePicker.show(it1, DATEPICKER_TAG) }

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

            activity?.let { timePicker.show(it.supportFragmentManager, null) }

        }

        binding.btnAddTask.setOnClickListener {
            getTaskFromForm()
            TaskDataSource.insertTask(task)
            goToListTasks()
        }

        binding.btnCancel.setOnClickListener {
            goToListTasks()
        }

    }

    private fun goToListTasks() {
        view?.let { it.findNavController().navigate(R.id.action_formTaskFragment_to_listTaskFragment) }
    }

    private fun loadTaskInForm(task: Task) {
        binding.tilTitle.text = task.title
        binding.tilDate.text = task.date
        binding.tilTime.text = task.hour
    }

    private fun getTaskFromForm() {
        task.title = binding.tilTitle.text
        task.date = binding.tilDate.text
        task.hour = binding.tilTime.text
    }

    companion object{
        val TASK_ID = "task"
        private val DATEPICKER_TAG = "DATEPICKER_TAG"
        private val TIMEPICKER_TAG = "TIMEPICKER_TAG"
        private const val NEW_TASK = 0
    }
}