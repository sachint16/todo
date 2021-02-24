package com.pectolabs.todo.ui.acitivities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import com.pectolabs.todo.R
import com.pectolabs.todo.databinding.ActivityAddTaskBinding
import com.pectolabs.todo.db.Task
import com.pectolabs.todo.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.Exception
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddTaskActivity : AppCompatActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private lateinit var binding: ActivityAddTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvAddTask.typeface = ResourcesCompat.getFont(this, R.font.avenir_ltstd_book)
        binding.btnAddTask.typeface = ResourcesCompat.getFont(this, R.font.avenir_ltstd_book)

        binding.btnAddTask.setOnClickListener {
            val title = binding.tilTitle.editText?.text.toString().trim()
            val description = binding.tilDescription.editText?.text.toString().trim()
            val scheduledAt = Date()

            Timber.d("Title: $title\nDesc: $description")
            if (title.isEmpty()){
                binding.tilTitle.error = "Please enter title"

            }else if (description.isEmpty()){
                binding.tilDescription.error = "Please enter description"

            }else{
                val task = Task(title = title, description = description, scheduledAt = scheduledAt, isCompleted = false)

                try {
                    mainViewModel.insertTask(task)
                    setResult(Activity.RESULT_OK)
                    finish()
                }catch (ex:Exception){
                    Timber.e(ex)
                }
            }



        }
    }
}