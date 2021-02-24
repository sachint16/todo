package com.pectolabs.todo.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.pectolabs.todo.R
import com.pectolabs.todo.adapters.TaskAdapter
import com.pectolabs.todo.databinding.FragmentDashboardBinding
import com.pectolabs.todo.db.Task
import com.pectolabs.todo.interfaces.TaskAdapterListener
import com.pectolabs.todo.ui.viewmodels.DashboardViewModel
import com.pectolabs.todo.utils.DashboardFilterType
import com.pectolabs.todo.views.MarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
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
class DashboardFragment : Fragment(), View.OnClickListener,TaskAdapterListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    @Inject
    lateinit var viewModel: DashboardViewModel

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var taskAdapter: TaskAdapter

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
            taskAdapter.submitList(it)
        })

    }


    private fun setupRecyclerView() = binding.rvTask.apply {
        taskAdapter = TaskAdapter(this@DashboardFragment)
        adapter = taskAdapter
        layoutManager = LinearLayoutManager(requireContext())
        addItemDecoration(MarginItemDecoration(16))
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
            binding.tvCompleted -> {
                println("called")
                viewModel.filterTasks(DashboardFilterType.COMPLETED)
            }
            binding.tvAllTask -> viewModel.filterTasks(DashboardFilterType.ALL_TASK)
        }
    }

    override fun onResume() {
        super.onResume()

        Timber.d("changed_list")

//        viewModel.filterTasks(DashboardFilterType.PENDING)
        binding.rvTask.adapter?.notifyDataSetChanged()
    }

    override fun onTaskClickListener(task: Task) {
//        viewModel.deleteTask(task)
        Toast.makeText(context,"ID : ${task.id}\n${task.title}",Toast.LENGTH_SHORT).show()
    }
}