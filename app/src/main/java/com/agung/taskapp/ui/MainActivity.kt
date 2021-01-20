package com.agung.taskapp.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.agung.taskapp.R
import com.agung.taskapp.adapter.TaskAdapter
import com.agung.taskapp.data.model.TaskModel
import com.agung.taskapp.database.TaskDatabase
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.HorizontalCalendarView
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener, AddTaskFragment.OnDialogListener, EditTaskFragment.OnDialogListener {

    private var taskDatabase:TaskDatabase? = null
    private lateinit var getDay: String
    private lateinit var tvTask: TextView
    private lateinit var countTask: String

    private lateinit var adapter: TaskAdapter
//    private lateinit var mainViewModel: MainViewModel
    private  var listTask: MutableList<TaskModel>? = null

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        taskDatabase = TaskDatabase.getInstance(this)

        val btnAddTask: LinearLayout = findViewById(R.id.btn_add_task)
        btnAddTask.setOnClickListener(this)

        tvTask = findViewById(R.id.txt_task_today)

        val dateToday: TextView = findViewById(R.id.txt_date_today)
//        val year = SimpleDateFormat("dd/M/yyyy hh:mm:ss EE EEE")
        val year = SimpleDateFormat("yyyy")
        val getYear = year.format(Date())
        val month = SimpleDateFormat("MMM")
        val getMonth = month.format(Date())
        val day = SimpleDateFormat("dd")
        getDay = day.format(Date())
        val dayOfWeek = SimpleDateFormat("EE")
        val getDayOfWeek = dayOfWeek.format(Date())
        dateToday.text = "$getDayOfWeek, $getMonth $getDay $getYear"

        showRecyclerTask()
        showCalendar()
    }

    @SuppressLint("SetTextI18n")
    private fun showRecyclerTask() {
        adapter = TaskAdapter(this)
        adapter.notifyDataSetChanged()
        rv_task.layoutManager = LinearLayoutManager(this)
        rv_task.adapter = adapter

        //get item task from room
        listTask = taskDatabase!!.taskDao().selectDate("$getDay%")
        adapter.setData(listTask as ArrayList<TaskModel>)

        //get count task
        countTask = listTask!!.size.toString()
        tvTask.text = "$countTask Task for Today"


    }

    private fun showCalendar() {
        /* starts before 1 month from now */
        val startDate: Calendar = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -1)

        /* ends after 1 month from now */
        val endDate: Calendar = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 1)

        val horizontalCalendar = HorizontalCalendar.Builder(this, R.id.calendarView)
            .range(startDate, endDate)
            .datesNumberOnScreen(7)
            .mode(HorizontalCalendar.Mode.DAYS)
            .configure()
            .formatTopText("")
            .formatMiddleText("EE")
            .formatBottomText("dd")
            .showBottomText(true)
            .sizeMiddleText(13F)
            .sizeBottomText(13F)
            .textColor(resources.getColor(R.color.black), resources.getColor(R.color.white))
            .selectedDateBackground(resources.getDrawable(R.drawable.semi_oval))
            .selectorColor(2)
            .end()
            .build()
        horizontalCalendar.defaultStyle.setBackground(resources.getDrawable(R.drawable.semi_oval_grey))


        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            @SuppressLint("SetTextI18n", "SimpleDateFormat")
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onDateSelected(date: Calendar?, position: Int) {
                val getDate = date?.get(Calendar.DAY_OF_MONTH)
                val getYear = date?.get(Calendar.YEAR)
                val getMonth = date?.get(Calendar.MONTH)?.plus(1)

                countTask = taskDatabase!!.taskDao().getDateCount("$getDate%").toString()
                val dateSelected = "$getDate/$getMonth/$getYear"
                val ds = SimpleDateFormat("dd/MM/yyyy").parse(dateSelected)
                val month = SimpleDateFormat("dd/MMM/yyy")
                val getMonthOfString = month.format(ds)
                tvTask.text = "$countTask Task for $getMonthOfString"

                //get list from room to adapter
                listTask = taskDatabase!!.taskDao().selectDate("$getDate%")
                adapter.notifyDataSetChanged()
                adapter.setData(listTask as ArrayList<TaskModel>)
                adapter.notifyDataSetChanged()
                println("tess $date")
//                Toast.makeText(this@MainActivity, "$date", Toast.LENGTH_SHORT).show()
            }
            override fun onCalendarScroll(calendarView: HorizontalCalendarView, dx: Int, dy: Int) {

            }

            override fun onDateLongClicked(date: Calendar?, position: Int): Boolean {
                return true
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_add_task -> {
                val mOptionDialogFragment = AddTaskFragment()
                val mFragmentManager = supportFragmentManager
                mOptionDialogFragment.show(mFragmentManager, AddTaskFragment::class.java.simpleName)
            }
        }
    }

    override fun onAddTask(text: String?) {
        finish()
        startActivity(getIntent())
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
    }

}