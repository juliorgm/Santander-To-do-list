package br.com.cuiadigital.todolist.ui.form_task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import br.com.cuiadigital.todolist.R
import br.com.cuiadigital.todolist.TaskApplication
import br.com.cuiadigital.todolist.databinding.FragmentFormTaskBinding
import br.com.cuiadigital.todolist.extensions.format
import br.com.cuiadigital.todolist.extensions.text
import br.com.cuiadigital.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class FormTaskFragment : Fragment() {
    private lateinit var binding: FragmentFormTaskBinding
    private val viewModel: FormTaskViewModel by activityViewModels{
        FormTaskViewModelFactory(
            (activity?.application as TaskApplication).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        insertListeners()
        initObserver()

        arguments?.let {
            val task = it.getParcelable(TASK_ID) ?: Task()

            if (isEditable(task))   {
                binding.btnAddTask.text = getString(R.string.btn_edit_task)
                viewModel.updateTask(task)
                viewModel.isEditable.value = true
            }

            if (!it.getString(TITLE).isNullOrEmpty()){
                val title= it.getString(TITLE) ?: ""
                val date= it.getString(DATE) ?: ""
                val hour= it.getString(HOUR) ?: ""
                viewModel.updateTask(title= title, date= date, hour= hour )
            }
        }
    }

    fun initObserver(){
        viewModel.task.observe(viewLifecycleOwner, {
            loadTaskInForm(it)
        })
    }

    private fun isEditable(task: Task): Boolean {
        return task.id != NEW_TASK
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

            if (viewModel.isEditable.value == true)  viewModel.update()
            else  viewModel.insert()

            goToListTasks()
        }

        binding.btnCancel.setOnClickListener {
            goToListTasks()
        }
    }

    private fun goToListTasks() {
        view?.findNavController()?.popBackStack(R.id.action_formTaskFragment_to_listTaskFragment, false)
        view?.findNavController()?.navigate(R.id.action_formTaskFragment_to_listTaskFragment)
    }

    private fun loadTaskInForm(task: Task) {
        binding.tilTitle.text = task.title
        binding.tilDate.text = task.date
        binding.tilTime.text = task.hour
    }

    private fun cleanForm() {
        binding.tilTitle.text = ""
        binding.tilDate.text = ""
        binding.tilTime.text = ""
    }

    private fun getTaskFromForm() {
        viewModel.updateTask(title = binding.tilTitle.text, hour= binding.tilTime.text, date= binding.tilDate.text)
    }

    override fun onDestroy() {
        super.onDestroy()
        arguments.let {
            it?.putString(TITLE, binding.tilTitle.text)
            it?.putString(DATE, binding.tilDate.text)
            it?.putString(HOUR, binding.tilTime.text)
        }
    }

    companion object{
        const val TASK_ID = "task"
        private const val DATEPICKER_TAG = "DATEPICKER_TAG"
        private const val NEW_TASK = 0
        private const val TITLE = "TITLE"
        private const val DATE = "DATE"
        private const val HOUR = "HOUR"
    }
}