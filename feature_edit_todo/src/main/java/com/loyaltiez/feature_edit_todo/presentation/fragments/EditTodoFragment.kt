package com.loyaltiez.feature_edit_todo.presentation.fragments

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.loyaltiez.core.broadcast_receivers.AlarmReceiver
import com.loyaltiez.core.data.data_source.TindoRoomDatabase
import com.loyaltiez.core.data.repository.ToDoDAO
import com.loyaltiez.core.domain.model.todo.ToDo
import com.loyaltiez.core.presentation.fragments.TinDoFragment
import com.loyaltiez.core.services.AlarmService
import com.loyaltiez.create_edit_todo_core.domain.ToDoColor
import com.loyaltiez.create_edit_todo_core.domain.TodoType
import com.loyaltiez.feature_edit_todo.EditTodoActivity
import com.loyaltiez.feature_edit_todo.R
import com.loyaltiez.feature_edit_todo.databinding.EditTodoFragmentBinding
import com.loyaltiez.feature_edit_todo.presentation.view_models.EditTodoViewModel
import java.sql.Date
import java.sql.Time
import java.util.*

class EditTodoFragment : TinDoFragment() {

    private val viewModel: EditTodoViewModel by lazy {

        // Instantiate using the Factory extension function
        ViewModelProvider(
            this,
            EditTodoViewModel.Factory(
                requireActivity().application,
                (requireActivity() as EditTodoActivity).todo,
                ToDoDAO(TindoRoomDatabase.invoke(requireContext()))
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

        setObservers()
        setListeners()

        return binding.root
    }

    private lateinit var pendingIntent: PendingIntent

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
            pendingIntent
        )
    }

    private fun setObservers() {

        setNavigationObservers()
        setErrorObservers()
        setPickerObservers()

        viewModel.newTodo.observe(
            viewLifecycleOwner
        ) {
            if (it != null) {

                if (it.date == null)
                    updateAlarm(it, "daily")
                else
                    updateAlarm(it, "weekly")
            }
        }
    }

    private fun setNavigationObservers() {

        observeFragmentToActivityNavigationFlag(
            viewModel.navigateToHome,
            { EditTodoFragmentDirections.actionEditTodoFragmentToHomeActivity() },
            viewModel::onNavigateToHomeComplete,
            false
        )
    }

    private fun setErrorObservers() {

        observeTextInputError(
            viewModel.titleInputState.error,
            binding.createEditTodoLayout.outlinedTextFieldTitle,
        ) { (viewModel.titleInputState::getError)(requireContext()) }

        observeTextInputError(
            viewModel.descriptionInputState.error,
            binding.createEditTodoLayout.outlinedTextFieldDescription,
        ) { (viewModel.descriptionInputState::getError)(requireContext()) }

        observeTextInputError(
            viewModel.remindMeAtState.error,
            binding.createEditTodoLayout.outlinedTextFieldRemindMeAt,
        ) { (viewModel.remindMeAtState::getError)(requireContext()) }

        observeTextInputError(
            viewModel.startingOnState.error,
            binding.createEditTodoLayout.outlinedTextFieldStartingOn,
        ) { (viewModel.startingOnState::getError)(requireContext()) }

        viewModel.todoType.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                TodoType.DAILY -> {
                    binding.createEditTodoLayout.outlinedTextFieldStartingOn.visibility = View.GONE
                }
                TodoType.WEEKLY -> {
                    binding.createEditTodoLayout.outlinedTextFieldStartingOn.visibility =
                        View.VISIBLE
                }
                else -> {
                    // This should never happen, graceful fail is needed based on requirements
                }
            }
        }
    }

    private fun setPickerObservers() {

        viewModel.showTimePicker.observe(
            viewLifecycleOwner
        ) {
            if (it) {

                val timePicker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("Remind Me At")
                        .build()

                timePicker.addOnPositiveButtonClickListener {
                    viewModel.onRemindMeAtTextChanged(Time(timePicker.hour, timePicker.minute, 0))
                }

                timePicker.show(parentFragmentManager, "TIME_PICKER")

                viewModel.onShowTimePickerComplete()
            }
        }

        viewModel.showDatePicker.observe(
            viewLifecycleOwner
        ) {
            if (it) {

                val constraintsBuilder =
                    CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.now())

                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Starting On")
                        .setCalendarConstraints(constraintsBuilder.build())
                        .setSelection(viewModel.startingOnState.input.value?.time)
                        .build()

                datePicker.addOnPositiveButtonClickListener {

                    viewModel.onStartingOnTextChanged(datePicker.selection?.let { selection ->
                        Date(
                            selection
                        )
                    })
                }

                datePicker.show(parentFragmentManager, "DATE_PICKER")

                viewModel.onShowDatePickerComplete()
            }
        }
    }

    private fun setListeners() {

        setTextInputListeners()
        setRadioButtonListeners()
    }

    private fun setTextInputListeners() {

        binding.createEditTodoLayout.outlinedTextFieldTitle.editText?.doOnTextChanged { inputText, _, _, _ ->

            viewModel.onTitleTextChanged(inputText.toString())
        }

        binding.createEditTodoLayout.outlinedTextFieldDescription.editText?.doOnTextChanged { inputText, _, _, _ ->

            viewModel.onDescriptionTextChanged(inputText.toString())
        }
    }

    private fun setRadioButtonListeners() {

        binding.createEditTodoLayout.radioGroupTodoType.setOnCheckedChangeListener { _, checkedId ->
            // Responds to child RadioButton checked/unchecked

            if (checkedId == binding.createEditTodoLayout.radioDaily.id)
                viewModel.onTodoTypeChanged(TodoType.DAILY)
            else
                viewModel.onTodoTypeChanged(TodoType.WEEKLY)
        }

        binding.createEditTodoLayout.radioGroupColors.setOnCheckedChangeListener { _, checkedId ->
            // Responds to child RadioButton checked/unchecked

            when (checkedId) {
                binding.createEditTodoLayout.radioRed.id -> viewModel.onTodoColorChanged(ToDoColor.RED)
                binding.createEditTodoLayout.radioYellow.id -> viewModel.onTodoColorChanged(
                    ToDoColor.YELLOW
                )
                binding.createEditTodoLayout.radioBlue.id -> viewModel.onTodoColorChanged(ToDoColor.BLUE)
                binding.createEditTodoLayout.radioGreen.id -> viewModel.onTodoColorChanged(ToDoColor.GREEN)
                binding.createEditTodoLayout.radioPink.id -> viewModel.onTodoColorChanged(ToDoColor.PINK)
                binding.createEditTodoLayout.radioOrange.id -> viewModel.onTodoColorChanged(
                    ToDoColor.ORANGE
                )
                binding.createEditTodoLayout.radioTeal.id -> viewModel.onTodoColorChanged(ToDoColor.TEAL)
                binding.createEditTodoLayout.radioWhite.id -> viewModel.onTodoColorChanged(ToDoColor.WHITE)
            }
        }
    }
}