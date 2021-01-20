package com.agung.taskapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.agung.taskapp.R
import com.agung.taskapp.data.model.TaskModel
import com.agung.taskapp.database.TaskDatabase
import com.agung.taskapp.ui.EditTaskFragment
import com.amulyakhare.textdrawable.util.ColorGenerator


class TaskAdapter(context: Context) : RecyclerView.Adapter<TaskAdapter.MainAdapterViewHolder>() {
    private val context = context
    private var taskDatabase: TaskDatabase? = null
    private var bundle: Bundle? = null

    var listTask = ArrayList<TaskModel>()
        set(listTask) {
            if (listTask.size > 0) {
                this.listTask.clear()
            }
            this.listTask.addAll(listTask)

            notifyDataSetChanged()
        }

    fun setData(items: ArrayList<TaskModel>?) {
        listTask.clear()
        if (items != null) {
            listTask.addAll(items)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapterViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_task_list,
            parent,
            false
        )
        return MainAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainAdapterViewHolder, position: Int) {
        val task = listTask[position]

        holder.title.text = task.task_tittle
        val generator: ColorGenerator = ColorGenerator.MATERIAL
        val color: Int = generator.getRandomColor()
        holder.cvTitle.setCardBackgroundColor(color)

        holder.overflow.setOnClickListener {
            val popupMenu = PopupMenu(context, it)
            popupMenu.inflate(R.menu.popup_menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete -> {
                        val alertDialogBuilder = AlertDialog.Builder(context)
                        alertDialogBuilder.setMessage(
                            "Are you sure delete " + task.task_tittle + " ?"
                        )
                        alertDialogBuilder.setPositiveButton(
                            "Yes"
                        ) { dialogInterface, i ->
                            taskDatabase = TaskDatabase.getInstance(context)
                            taskDatabase?.taskDao()?.delete(task)

                            listTask.removeAt(position)

                            notifyItemRemoved(position)
                            notifyItemRangeChanged(0, listTask.size)
                            Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT)
                                .show()
                        }
                        alertDialogBuilder.setNegativeButton(
                            "No"
                        ) { dialogInterface, i -> }
                        val alertDialog = alertDialogBuilder.create()
                        alertDialog.show()
                    }
                    R.id.edit -> {
                        val dialog = EditTaskFragment()
                        val bundle = Bundle()
                        bundle.putString("id_task", task.id_task.toString())
                        bundle.putString("title_task", task.task_tittle.toString())
                        bundle.putString("description_task", task.task_description.toString())
                        bundle.putString("date_task", task.task_date.toString())
                        dialog.arguments = bundle
                        dialog.show((context as AppCompatActivity).supportFragmentManager, "EditTaskFragment")

                    }
                }
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return listTask.size
    }

    inner class MainAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.txt_title)
        var cvTitle: CardView = itemView.findViewById(R.id.cv_title)
        var overflow: ImageView = itemView.findViewById(R.id.overflow)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: TaskModel?, v: View?)
    }
}