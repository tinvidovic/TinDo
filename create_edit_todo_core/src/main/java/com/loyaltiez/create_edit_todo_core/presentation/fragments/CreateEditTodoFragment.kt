package com.loyaltiez.create_edit_todo_core.presentation.fragments

import android.view.View
import androidx.core.widget.doOnTextChanged
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.loyaltiez.core.domain.model.todo.ToDoColor
import com.loyaltiez.core.domain.model.todo.TodoType
import com.loyaltiez.core.presentation.fragments.TinDoFragment
import com.loyaltiez.create_edit_todo_core.databinding.CreateEditTodoLayoutBinding
import com.loyaltiez.create_edit_todo_core.presentation.view_models.CreateEditTodoViewModel
import java.sql.Date
import java.sql.Time

/*
The common Fragment from which the Create and Edit Fragment inherit
 */
abstract class CreateEditTodoFragment : TinDoFragment() {

    abstract val viewModel :CreateEditTodoViewModel

    protected open fun setObservers(binding: CreateEditTodoLayoutBinding) {

        setErrorObservers(binding)

        setPickerObservers()
    }

    private fun setErrorObservers(binding: CreateEditTodoLayoutBinding) {

        observeTextInputError(
            viewModel.startingOnState.error,
            binding.outlinedTextFieldStartingOn,
            binding.scrollView
        ) { (viewModel.startingOnState::getError)(requireContext()) }

        observeTextInputError(
            viewModel.remindMeAtState.error,
            binding.outlinedTextFieldRemindMeAt,
            binding.scrollView
        ) { (viewModel.remindMeAtState::getError)(requireContext()) }

        observeTextInputError(
            viewModel.descriptionInputState.error,
            binding.outlinedTextFieldDescription,
            binding.scrollView
        ) { (viewModel.descriptionInputState::getError)(requireContext()) }

        observeTextInputError(
            viewModel.titleInputState.error,
            binding.outlinedTextFieldTitle,
            binding.scrollView
        ) { (viewModel.titleInputState::getError)(requireContext()) }

        viewModel.todoType.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                TodoType.DAILY -> {
                    binding.outlinedTextFieldStartingOn.visibility = View.GONE
                }
                TodoType.WEEKLY -> {
                    binding.outlinedTextFieldStartingOn.visibility =
                        View.VISIBLE
                }
                else -> {
                    // This should never happen, graceful fail is needed based on requirements
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun setPickerObservers() {

        viewModel.showTimePicker.observe(
            viewLifecycleOwner
        ) {
            if (it) {

                val currentlySelectedHour =
                    (viewModel.remindMeAtState.input.value ?: Time(12, 0, 0)).hours
                val currentlySelectedMinute =
                    (viewModel.remindMeAtState.input.value ?: Time(12, 0, 0)).minutes

                val timePicker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(currentlySelectedHour)
                        .setMinute(currentlySelectedMinute)
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

    protected fun setListeners(binding: CreateEditTodoLayoutBinding) {

        setTextInputListeners(binding)
        setRadioButtonListeners(binding)
    }

    private fun setTextInputListeners(binding: CreateEditTodoLayoutBinding) {

        binding.outlinedTextFieldTitle.editText?.doOnTextChanged { inputText, _, _, _ ->

            viewModel.onTitleTextChanged(inputText.toString())
        }

        binding.outlinedTextFieldDescription.editText?.doOnTextChanged { inputText, _, _, _ ->

            viewModel.onDescriptionTextChanged(inputText.toString())
        }
    }

    private fun setRadioButtonListeners(binding: CreateEditTodoLayoutBinding) {

        binding.radioGroupTodoType.setOnCheckedChangeListener { _, checkedId ->
            // Responds to child RadioButton checked/unchecked

            if (checkedId == binding.radioDaily.id)
                viewModel.onTodoTypeChanged(TodoType.DAILY)
            else
                viewModel.onTodoTypeChanged(TodoType.WEEKLY)
        }

        binding.radioGroupColors.setOnCheckedChangeListener { _, checkedId ->
            // Responds to child RadioButton checked/unchecked

            when (checkedId) {
                binding.radioRed.id -> viewModel.onTodoColorChanged(ToDoColor.RED)
                binding.radioYellow.id -> viewModel.onTodoColorChanged(
                    ToDoColor.YELLOW
                )
                binding.radioBlue.id -> viewModel.onTodoColorChanged(ToDoColor.BLUE)
                binding.radioGreen.id -> viewModel.onTodoColorChanged(ToDoColor.GREEN)
                binding.radioPink.id -> viewModel.onTodoColorChanged(ToDoColor.PINK)
                binding.radioOrange.id -> viewModel.onTodoColorChanged(
                    ToDoColor.ORANGE
                )
                binding.radioTeal.id -> viewModel.onTodoColorChanged(ToDoColor.TEAL)
                binding.radioWhite.id -> viewModel.onTodoColorChanged(ToDoColor.WHITE)
            }
        }
    }
}