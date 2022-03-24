package com.loyaltiez.feature_home.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.loyaltiez.core.data.data_source.TindoRoomDatabase
import com.loyaltiez.core.data.repository.ToDoDAO
import com.loyaltiez.core.domain.model.todo.DailyToDo
import com.loyaltiez.core.domain.model.todo.ToDo
import com.loyaltiez.core.domain.model.todo.WeeklyToDo
import com.loyaltiez.core.presentation.fragments.TinDoFragment
import com.loyaltiez.feature_home.R
import com.loyaltiez.feature_home.adapters.DeleteTindoItemClickListener
import com.loyaltiez.feature_home.adapters.EditTindoItemClickListener
import com.loyaltiez.feature_home.adapters.TindoItemAdapter
import com.loyaltiez.feature_home.databinding.HomeFragmentBinding
import com.loyaltiez.feature_home.presentation.view_models.HomeViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment : TinDoFragment() {

    private val viewModel: HomeViewModel by lazy {

        // Instantiate using the Factory extension function
        ViewModelProvider(
            this,
            HomeViewModel.Factory(
                requireActivity().application,
                ToDoDAO(TindoRoomDatabase.invoke(requireContext()))
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
            },
            DeleteTindoItemClickListener { tindo ->
                viewModel.onDeleteTindoClicked(tindo)
            }
        )

        binding.recyclerViewTodos.adapter = adapter

    }

    private fun setObservers() {

        setNavigationObservers()

        lifecycle.coroutineScope.launch {
            viewModel.getDailyToDos().collect {
                val list = mutableListOf<ToDo>()

                for (todo in it ){
                    if (todo.date == null){
                        list.add(DailyToDo(todo.userEmail, todo.title, todo.description, todo.color
                        , todo.time, todo.id))
                    } else {
                        list.add(WeeklyToDo(todo.userEmail, todo.title, todo.description, todo.color
                            , todo.time, todo.date, todo.id))
                    }
                }
                adapter.submitList(list)
            }
        }
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