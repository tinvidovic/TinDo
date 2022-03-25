package com.loyaltiez.feature_edit_todo.presentation.fragments

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.loyaltiez.core.broadcast_receivers.AlarmReceiver
import com.loyaltiez.core.data.data_source.TindoRoomDatabase
import com.loyaltiez.core.data.repository.ToDoDAO
import com.loyaltiez.core.domain.model.todo.DailyToDo
import com.loyaltiez.core.domain.model.todo.ToDo
import com.loyaltiez.core.domain.model.todo.WeeklyToDo
import com.loyaltiez.core.services.AlarmService
import com.loyaltiez.create_edit_todo_core.databinding.CreateEditTodoLayoutBinding
import com.loyaltiez.create_edit_todo_core.presentation.fragments.CreateEditTodoFragment
import com.loyaltiez.feature_edit_todo.EditTodoActivity
import com.loyaltiez.feature_edit_todo.R
import com.loyaltiez.feature_edit_todo.databinding.EditTodoFragmentBinding
import com.loyaltiez.feature_edit_todo.presentation.view_models.EditTodoViewModel
import java.util.*

class EditTodoFragment : CreateEditTodoFragment() {

    override val viewModel: EditTodoViewModel by lazy {

        // Instantiate using the Factory extension function
        ViewModelProvider(
            this,
            EditTodoViewModel.Factory(
                requireActivity().application,
                (requireActivity() as EditTodoActivity).todo
            )
        )
            .get(EditTodoViewModel::class.java)
    }

    private lateinit var binding: EditTodoFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Create the binding variable
        binding = DataBindingUtil
            .inflate(
                inflater,
                R.layout.edit_todo_fragment,
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

    private lateinit var pendingIntent: PendingIntent

    @Suppress("DEPRECATION")
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun updateAlarm(toDo: ToDo, type: String) {

        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, toDo.time.hours)
            set(Calendar.MINUTE, toDo.time.minutes)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val alarmService = AlarmService(requireContext())

        val intent = Intent(requireActivity(), AlarmReceiver::class.java)

        intent.putExtra("title", toDo.title)
        intent.putExtra("description", toDo.description)
        intent.putExtra("time", toDo.getTimeString())
        intent.putExtra("id", toDo.id)
        intent.putExtra("type", type)

        pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            toDo.id!!,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmService.setAlarm(
            calendar.timeInMillis,
            pendingIntent,
            toDo.id!!
        )
    }

    override fun setObservers(binding: CreateEditTodoLayoutBinding) {

        super.setObservers(binding)

        setNavigationObservers()

        viewModel.newTodo.observe(
            viewLifecycleOwner
        ) {
            if (it != null) {

                if (it.date == null)
                    updateAlarm(it, DailyToDo.getTypeString())
                else
                    updateAlarm(it, WeeklyToDo.getTypeString())
            }
        }
    }

    private fun setNavigationObservers() {

        observeFragmentToActivityNavigationFlag(
            viewModel.navigateToHome,
            { EditTodoFragmentDirections.actionEditTodoFragmentToHomeActivity() },
            viewModel::onNavigateToHomeComplete,
            true
        )
    }
}