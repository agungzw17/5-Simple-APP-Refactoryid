package com.agung.taskapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.agung.taskapp.R
import com.agung.taskapp.data.model.TaskModel
import com.agung.taskapp.database.TaskDatabase
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log


class AddTaskFragment : DialogFragment() {
    private lateinit var inputTitle: TextInputEditText
    private lateinit var inputDescription: TextInputEditText
    private lateinit var inputDate: String
    private lateinit var txtDate: TextView
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private lateinit var calendar: CalendarView

    private var taskDatabase:TaskDatabase? = null
    private var onDialogListener: OnDialogListener? = null


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputTitle = view.findViewById(R.id.input_title)
        inputDescription = view.findViewById(R.id.input_description)
        txtDate = view.findViewById(R.id.txt_date)
        txtDate.text = null
        calendar = view.findViewById(R.id.calendar)
        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val finalMonth = month+1
            val stringDate:String = "$dayOfMonth/$finalMonth/$year"
//            inputDate = SimpleDateFormat("dd/MM/yyyy").parse(stringDate)
            inputDate = stringDate
            val date = "$dayOfMonth/$finalMonth/$year"
            txtDate.text = date
        }

        btnSave = view.findViewById(R.id.btn_save)
        btnCancel = view.findViewById(R.id.btn_cancel)
        btnSave.setOnClickListener {
            val title = inputTitle.text.toString()
            val description = inputDescription.text.toString()
            val date = txtDate.text.toString()

            var isEmptyFields = false

            if (title.isEmpty()) {
                isEmptyFields = true
                inputTitle.error = "Title cannot be empty."
            }

            if (description.isEmpty()) {
                isEmptyFields = true
                inputDescription.error = "Description cannot be empty."
            }

            if (date.isEmpty()) {
                isEmptyFields = true
                txtDate.error = "Date cannot be empty."
            }

            if (title.isNotEmpty() && description.isNotEmpty() && date.isNotEmpty()) {
                saveData(inputDate)
                val coach: String? = "Task has been added"
                onDialogListener?.onAddTask(coach)
                dialog?.dismiss()
            }
        }
        btnCancel.setOnClickListener {
            dialog?.cancel()
        }
    }

    private fun saveData(inputDate: String){
        taskDatabase = TaskDatabase.getInstance(requireContext())
        val taskModel = TaskModel(
                task_tittle = inputTitle.text.toString(),
                task_description = inputDescription.text.toString(),
                task_date = inputDate
        )
        taskDatabase?.taskDao()?.insert(taskModel)

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the EditNameDialogListener so we can send events to the host
            onDialogListener = context as OnDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException("$context must implement OnOptionDialogListener")
        }
    }


    override fun onDetach() {
        super.onDetach()
        this.onDialogListener = null
    }

    interface OnDialogListener {
        fun onAddTask(text: String?)
    }
}