package br.com.cuiadigital.todolist.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.cuiadigital.todolist.R
import br.com.cuiadigital.todolist.databinding.ItemTaskBinding
import br.com.cuiadigital.todolist.model.Task

class TaskAdapter: RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    var listenerEdit : (Task) -> Unit = {}
    var listenerDelete: (Task) -> Unit = {}

    val list = mutableListOf<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(view, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun updateList(list: List<Task>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.tvTitle.text = task.title
            binding.tvData.text = "${task.date} ${task.hour}"
            binding.imgMore.setOnClickListener {
                showPopup(task)
            }
        }

        private fun showPopup(task: Task) {
            val popupMenu = PopupMenu(binding.imgMore.context, binding.imgMore)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.action_edit -> listenerEdit(task)
                    R.id.action_delete -> listenerDelete(task)
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }
    }
}