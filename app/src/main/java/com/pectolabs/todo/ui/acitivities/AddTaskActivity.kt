package com.pectolabs.todo.ui.acitivities

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.pectolabs.todo.R
import com.pectolabs.todo.databinding.ActivityAddTaskBinding
import com.pectolabs.todo.db.Task
import com.pectolabs.todo.ui.viewmodels.MainViewModel
import com.pectolabs.todo.utils.DateUtils
import com.pectolabs.todo.utils.showToast
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddTaskActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private var task: Task? = null

    private lateinit var binding: ActivityAddTaskBinding

    private var taskDate: Date? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        setContentView(binding.root)

        val bundle = intent.getBundleExtra("bundle")

        bundle?.getParcelable<Task>("task")?.let {
            task = it
            binding.tvAddTask.text = "Update Task"
            binding.tilTitle.editText?.setText(it.title)
            binding.tilDescription.editText?.setText(it.description)
            binding.btnAddTask.text = "Update Task"
        }

        binding.tvAddTask.typeface = ResourcesCompat.getFont(this, R.font.avenir_ltstd_book)
        binding.btnAddTask.typeface = ResourcesCompat.getFont(this, R.font.avenir_ltstd_book)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnAddTask.setOnClickListener {
            val title = binding.tilTitle.editText?.text.toString().trim()
            val description = binding.tilDescription.editText?.text.toString().trim()

            Timber.d("Title: $title\nDesc: $description\nDate: $taskDate")
            if (title.isEmpty()) {
                binding.tilTitle.error = "Please enter title"

            } else if (description.isEmpty()) {
                binding.tilDescription.error = "Please enter description"

            } else if (taskDate == null) {
                showToast("Please select date")
            } else {
                val taskInfo = Task(
                    title = title,
                    description = description,
                    scheduledAt = taskDate!!,
                    isCompleted = false
                )

                try {
                    if (task != null) {
                        taskInfo.id = task?.id
                        mainViewModel.updateTask(taskInfo)
                    } else {
                        mainViewModel.insertTask(taskInfo)
                    }

                    setResult(Activity.RESULT_OK)
                    finish()
                } catch (ex: Exception) {
                    Timber.e(ex)
                }
            }
        }

        binding.btnToday.setOnClickListener(this)
        binding.btnTomorrow.setOnClickListener(this)
        binding.btnSelectDate.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        taskDate = null
        binding.btnToday.setTextColor(resources.getColor(R.color.black))
        binding.btnTomorrow.setTextColor(resources.getColor(R.color.black))
        binding.btnSelectDate.setTextColor(resources.getColor(R.color.black))

        when (view) {
            binding.btnToday -> {
                binding.btnToday.setTextColor(resources.getColor(R.color.wild_watermelon))
                taskDate = Calendar.getInstance().time
                binding.btnToday.text = "Today\n${DateUtils.fromDateObjectToString(taskDate!!)}"
            }

            binding.btnTomorrow -> {
                binding.btnTomorrow.setTextColor(resources.getColor(R.color.wild_watermelon))
                val tomorrow = Calendar.getInstance()
                tomorrow.add(Calendar.DATE, 1)
                taskDate = tomorrow.time
                binding.btnTomorrow.text = "Tomorrow\n${DateUtils.fromDateObjectToString(taskDate!!)}"
            }

            binding.btnSelectDate -> {
                val now = Calendar.getInstance()
                val datePickerDialog = DatePickerDialog.newInstance(
                    this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
                )
                datePickerDialog.minDate = Calendar.getInstance()
                datePickerDialog.show(supportFragmentManager, "Select Date")
            }
        }
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        binding.btnSelectDate.setTextColor(resources.getColor(R.color.wild_watermelon))

        val scheduledAt = Calendar.getInstance()
        scheduledAt.set(year, monthOfYear, dayOfMonth)
        taskDate = scheduledAt.time
        binding.btnSelectDate.text = "${DateUtils.fromDateObjectToString(taskDate!!)}"
    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        TODO("Not yet implemented")
    }
}