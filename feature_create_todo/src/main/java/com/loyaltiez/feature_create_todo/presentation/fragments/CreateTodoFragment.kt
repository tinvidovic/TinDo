package com.loyaltiez.feature_create_todo.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.loyaltiez.create_edit_todo_core.databinding.CreateEditTodoLayoutBinding
import com.loyaltiez.create_edit_todo_core.presentation.fragments.CreateEditTodoFragment
import com.loyaltiez.feature_create_todo.R
import com.loyaltiez.feature_create_todo.databinding.CreateTodoFragmentBinding
import com.loyaltiez.feature_create_todo.presentation.view_models.CreateTodoViewModel

class CreateTodoFragment : CreateEditTodoFragment() {

    override val viewModel: CreateTodoViewModel by lazy {

        // Instantiate using the Factory extension function
        ViewModelProvider(
            this,
            CreateTodoViewModel.Factory(
                requireActivity().application
            )
        )
            .get(CreateTodoViewModel::class.java)
    }

    private lateinit var binding: CreateTodoFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Create the binding variable
        binding = DataBindingUtil
            .inflate(
                inflater,
                R.layout.create_todo_fragment,
                container,
                false
            )

        // Set the lifecycle owner so that live data can be observed
        binding.lifecycleOwner = viewLifecycleOwner

        // Set the viewModel for the binding variable
        binding.viewModel = viewModel

        setObservers(binding.createEditTodoLayout)
        setListeners(binding.createEditTodoLayout)

        return binding.root
    }

    override fun setObservers(binding: CreateEditTodoLayoutBinding) {
        super.setObservers(binding)

        setNavigationObservers()
    }

    private fun setNavigationObservers() {

        observeFragmentToActivityNavigationFlag(
            viewModel.navigateToHome,
            { CreateTodoFragmentDirections.actionCreateTodoFragmentToHomeActivity() },
            viewModel::onNavigateToHomeComplete,
            true
        )
    }

}