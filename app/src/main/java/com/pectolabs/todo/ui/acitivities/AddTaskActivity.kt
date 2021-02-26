package com.pectolabs.todo.ui.acitivities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import com.pectolabs.todo.R
import com.pectolabs.todo.databinding.ActivityAddTaskBinding
import com.pectolabs.todo.db.Task
import com.pectolabs.todo.ui.viewmodels.MainViewModel
import com.pectolabs.todo.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.Exception
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddTaskActivity : AppCompatActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private var task: Task? = null

    private lateinit var binding: ActivityAddTaskBinding
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

        binding.btnAddTask.setOnClickListener {
            val title = binding.tilTitle.editText?.text.toString().trim()
            val description = binding.tilDescription.editText?.text.toString().trim()
            val scheduledAt = Date()

            Timber.d("Title: $title\nDesc: $description")
            if (title.isEmpty()) {
                binding.tilTitle.error = "Please enter title"

            } else if (description.isEmpty()) {
                binding.tilDescription.error = "Please enter description"

            } else {
                val taskInfo = Task(title = title, description = description, scheduledAt = scheduledAt, isCompleted = false)

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
    }
}