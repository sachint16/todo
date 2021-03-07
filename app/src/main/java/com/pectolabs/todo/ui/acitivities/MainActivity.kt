package com.pectolabs.todo.ui.acitivities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.pectolabs.todo.R
import com.pectolabs.todo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val addTaskActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
//            Snackbar.make(binding.root, "Task added", Snackbar.LENGTH_SHORT).show()
            Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show()
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
//            Snackbar.make(binding.root, "Add task cancelled", Snackbar.LENGTH_SHORT).show()
//            Toast.makeText(this, "Add task cancelled", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.fragment))
        binding.bottomNavigationView.setOnNavigationItemReselectedListener { /*NO-OP*/ }



        binding.fabAddTask.setOnClickListener {
            addTaskActivityResult.launch(Intent(this, AddTaskActivity::class.java))
        }
    }
}