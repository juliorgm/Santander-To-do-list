package br.com.cuiadigital.todolist.ui.list_task

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.cuiadigital.todolist.R
import br.com.cuiadigital.todolist.databinding.FragmentListTaskBinding
import br.com.cuiadigital.todolist.datasource.TaskDataSource
import br.com.cuiadigital.todolist.model.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ListTaskFragment : Fragment() {
    private lateinit var binding: FragmentListTaskBinding
    private lateinit var adapter : TaskAdapter
    private val viewModel: ListTaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListTaskBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TaskAdapter()
        binding.rvTasks.setHasFixedSize(true)
        binding.rvTasks.layoutManager = LinearLayoutManager(context)
        binding.rvTasks.adapter = adapter

        insertListerner()
        initObserver()
    }

    private fun updateList(list: List<Task>) {
        binding.layoutEmptyState.emptyState.visibility = if(list.isEmpty()) View.VISIBLE else View.GONE
        adapter.updateList(list)
    }

    fun initObserver(){
        viewModel.tasklist.observe(viewLifecycleOwner, {
            updateList(it)
            Log.d("listtaskfragment","observer")
        })
    }

    private fun insertListerner() {
        binding.fabNewTask.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_listTaskFragment_to_formTaskFragment)
        }
        adapter.listenerEdit = { task ->
            val action = ListTaskFragmentDirections.actionListTaskFragmentToFormTaskFragment(task = task)
            view?.findNavController()?.navigate(action)
        }
        adapter.listenerDelete = {
            showDialog(it)
        }
    }

    private fun deleteTask(it: Task) {
        viewModel.deleteTask(it)
        adapter.updateList(TaskDataSource.getList())
    }

    private fun showDialog(task: Task){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_alert_title))
            .setMessage(getString(R.string.dialog_delete_message))
            .setPositiveButton(getString(R.string.dialog_yes)){_,_ -> deleteTask(task)}
            .setNegativeButton(getString(R.string.dialog_no)){_,_ -> return@setNegativeButton}
            .setCancelable(false)
            .show()
    }
}