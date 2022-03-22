package com.loyaltiez.feature_home.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.loyaltiez.core.domain.DailyToDo
import com.loyaltiez.core.domain.WeeklyToDo
import com.loyaltiez.core.presentation.fragments.TinDoFragment
import com.loyaltiez.feature_home.R
import com.loyaltiez.feature_home.adapters.EditTindoItemClickListener
import com.loyaltiez.feature_home.adapters.TindoItemAdapter
import com.loyaltiez.feature_home.databinding.HomeFragmentBinding
import com.loyaltiez.feature_home.presentation.view_models.HomeViewModel
import java.sql.Date
import java.sql.Time

class HomeFragment : TinDoFragment() {

    private val viewModel: HomeViewModel by lazy {

        // Instantiate using the Factory extension function
        ViewModelProvider(
            this,
            HomeViewModel.Factory(
                requireActivity().application
            )
        )
            .get(HomeViewModel::class.java)
    }

    private lateinit var binding: HomeFragmentBinding

    // The recyclerview adapter
    private lateinit var adapter: TindoItemAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Create the binding variable
        binding = DataBindingUtil
            .inflate(
                inflater,
                R.layout.home_fragment,
                container,
                false
            )

        // Set the lifecycle owner so that live data can be observed
        binding.lifecycleOwner = viewLifecycleOwner

        // Set the viewModel for the binding variable
        binding.viewModel = viewModel

        setAdapters()
        setObservers()

        return binding.root
    }

    private fun setAdapters() {

        adapter = TindoItemAdapter(
            requireActivity().application,
            EditTindoItemClickListener { tindo ->
                viewModel.onEditTindoClicked(tindo)
            }
        )

        binding.recyclerViewTodos.adapter = adapter

        adapter.submitList(
            listOf(
                DailyToDo(
                    "Daily ToDo",
                    "This is a dummy daily ToDo",
                    R.color.tinDoBlue,
                    Time(4, 0, 0)
                ),
                WeeklyToDo(
                    "Weekly ToDo",
                    "This is a dummy weekly ToDo",
                    R.color.tinDoGreen,
                    Time(4, 0, 0),
                    Date(2022, 5, 3)
                )
            )
        )
    }

    private fun setObservers() {

        setNavigationObservers()
    }

    private fun setNavigationObservers() {

        observeFragmentToActivityNavigationFlag(
            viewModel.navigateToCreateToDo,
            { HomeFragmentDirections.actionHomeFragmentToCreateTodoActivity() },
            viewModel::onNavigateToCreateToDoComplete,
            false
        )

        viewModel.navigateToEditToDo.observe(
            viewLifecycleOwner
        ){
            if (it != null){

                val navController = findNavController()

                navController.navigate(
                    HomeFragmentDirections.actionHomeFragmentToEditTodoActivity(it)
                )

                viewModel.onNavigateToEditToDoComplete()
            }
        }
    }

}