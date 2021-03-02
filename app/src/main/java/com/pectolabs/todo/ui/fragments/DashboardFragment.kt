package com.pectolabs.todo.ui.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat.animate
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.pectolabs.todo.R
import com.pectolabs.todo.adapters.TaskAdapter
import com.pectolabs.todo.databinding.FragmentDashboardBinding
import com.pectolabs.todo.db.Task
import com.pectolabs.todo.interfaces.TaskAdapterListener
import com.pectolabs.todo.ui.acitivities.AddTaskActivity
import com.pectolabs.todo.ui.viewmodels.DashboardViewModel
import com.pectolabs.todo.utils.DashboardFilterType
import com.pectolabs.todo.utils.showToast
import com.pectolabs.todo.views.MarginItemDecoration
import com.pectolabs.todo.views.SwipeToDeleteCallback
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class DashboardFragment : Fragment(), View.OnClickListener, TaskAdapterListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    @Inject
    lateinit var viewModel: DashboardViewModel

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var taskAdapter: TaskAdapter

    private var dashboardFilterType = DashboardFilterType.PENDING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.tvPending.setOnClickListener(this)
        binding.tvCompleted.setOnClickListener(this)
        binding.tvAllTask.setOnClickListener(this)

        viewModel.tasks.observe(viewLifecycleOwner, {
            if(it.isEmpty()){
                binding.rvTask.apply {
                    animate().alpha(0.0f)
                    visibility = View.GONE
                }

                binding.emptyView.root.apply {
                    animate().alpha(1.0f)
                    visibility = View.VISIBLE
                }
            }else{
                binding.emptyView.root.apply {
                    animate().alpha(0.0f)
                    visibility = View.GONE
                }

                binding.rvTask.apply {
                    animate().alpha(1.0f)
                    visibility = View.VISIBLE
                }
                taskAdapter.submitList(it)
            }

        })

    }


    private fun setupRecyclerView() {
        binding.rvTask.apply {
            taskAdapter = TaskAdapter(this@DashboardFragment)
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(MarginItemDecoration(16))
            adapter = taskAdapter

        }


        val swipeHandler = object : SwipeToDeleteCallback(
                requireContext(),
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.rvTask.adapter as TaskAdapter
                if (direction == ItemTouchHelper.LEFT) {
                    adapter.removeAt(viewHolder.adapterPosition)
                } else {
                    adapter.updateAt(viewHolder.adapterPosition)
                }
            }
        }

        //configure left swipe
        swipeHandler.leftBG = ContextCompat.getColor(requireContext(), R.color.wild_watermelon)
        swipeHandler.leftLabel = "Delete"
        swipeHandler.leftIcon =
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_delete)

        //configure right swipe
        swipeHandler.rightBG = ContextCompat.getColor(requireContext(), R.color.green)
        swipeHandler.rightLabel = "Archive"
        swipeHandler.rightIcon =
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_check)

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvTask)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                DashboardFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onClick(view: View?) {
        binding.tvPending.setTextColor(resources.getColor(R.color.viloent_voilet))
        binding.tvCompleted.setTextColor(resources.getColor(R.color.viloent_voilet))
        binding.tvAllTask.setTextColor(resources.getColor(R.color.viloent_voilet))

        (view as TextView).setTextColor(resources.getColor(R.color.wild_watermelon))

        when (view) {

            binding.tvPending -> viewModel.filterTasks(DashboardFilterType.PENDING)
            binding.tvCompleted -> viewModel.filterTasks(DashboardFilterType.COMPLETED)
            binding.tvAllTask -> viewModel.filterTasks(DashboardFilterType.ALL_TASK)
        }
    }

    private val editTaskActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    context.showToast("Task Updated")
                } else if (result.resultCode == Activity.RESULT_CANCELED) {
                }

            }

    override fun onTaskClickListener(task: Task) {
        if(!task.isCompleted){
            val editTaskIntent = Intent(context, AddTaskActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("task", task)
            bundle.putParcelable("position", task)
            editTaskIntent.putExtra("bundle", bundle)
            editTaskActivityResult.launch(editTaskIntent)
        }
    }

    override fun onRemoveItem(task: Task, position: Int) {

        var isTaskToBeDeleted = true

        val snackbar = Snackbar.make(
                binding.rvTask, "Task Deleted Successfully!",
                Snackbar.LENGTH_LONG
        ).setAction("Undo", View.OnClickListener {
            isTaskToBeDeleted = false
            val adapter = binding.rvTask.adapter as TaskAdapter
            adapter.tasks.add(position, task)
            adapter.undoView(position)
            binding.rvTask.scrollToPosition(position)
        }).setActionTextColor(Color.YELLOW)

        snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onShown(transientBottomBar: Snackbar?) {

            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                if (isTaskToBeDeleted) {
                    viewModel.deleteTask(task)
                }
            }
        })
        snackbar.show()
    }

    override fun onUpdateItem(task: Task, position: Int) {
        var isTaskToBeUpdated = true

        val snackbar = Snackbar.make(
                binding.rvTask, "Task Completed!",
                Snackbar.LENGTH_LONG
        ).setAction("Undo", View.OnClickListener {
            isTaskToBeUpdated = false
            val adapter = binding.rvTask.adapter as TaskAdapter
            adapter.tasks.add(position, task)
            adapter.undoView(position)
            binding.rvTask.scrollToPosition(position)
        }).setActionTextColor(Color.YELLOW)

        snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onShown(transientBottomBar: Snackbar?) {

            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                if (isTaskToBeUpdated) {
                    task.isCompleted = true
                    task.completedAt = Calendar.getInstance().time
                    viewModel.updateTask(task)
                }
            }
        })
        snackbar.show()
    }


}