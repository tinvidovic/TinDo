package com.loyaltiez.create_edit_todo_core.presentation.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loyaltiez.core.TindoApplication
import com.loyaltiez.core.domain.model.todo.ToDoColor
import com.loyaltiez.core.domain.model.todo.TodoType
import com.loyaltiez.core.presentation.view_models.input_states.DescriptionInputState
import com.loyaltiez.core.presentation.view_models.input_states.RemindMeAtState
import com.loyaltiez.core.presentation.view_models.input_states.StartingOnState
import com.loyaltiez.core.presentation.view_models.input_states.TitleInputState
import com.loyaltiez.create_edit_todo_core.di.CreateEditToDoActivityContainer
import java.sql.Date
import java.sql.Time

/*
The common ViewModel from which the Create and Edit TodoViewModels inherit
 */
open class CreateEditTodoViewModel(mApplication: Application) :
    AndroidViewModel(mApplication) {

    // NAVIGATION:
    protected val mNavigateToHome = MutableLiveData(false)
    val navigateToHome: LiveData<Boolean>
        get() = mNavigateToHome

    // INPUT STATES:
    val titleInputState = TitleInputState("")
    val descriptionInputState = DescriptionInputState("")

    // TINDO TYPE:
    protected val mTodoType = MutableLiveData(TodoType.DAILY)
    val todoType: LiveData<TodoType>
        get() = mTodoType

    // TINDO COLOR:
    protected val mTodoColor = MutableLiveData(ToDoColor.RED)
    val todoColor: LiveData<ToDoColor>
        get() = mTodoColor

    // TIME:
    val remindMeAtState = RemindMeAtState(null, mApplication.applicationContext)
    val startingOnState = StartingOnState(null, mApplication.applicationContext)

    // USE CASES:
    protected val insertToDoUseCase =
        ((mApplication as TindoApplication).appContainer as CreateEditToDoActivityContainer).insertToDoUseCase

    // UTILITY:
    private val mShowTimePicker = MutableLiveData(false)
    val showTimePicker: LiveData<Boolean>
        get() = mShowTimePicker

    private val mShowDatePicker = MutableLiveData(false)
    val showDatePicker: LiveData<Boolean>
        get() = mShowDatePicker

    // NAVIGATION:
    fun onNavigateToHomeComplete() {

        mNavigateToHome.value = false
    }

    // INPUT HANDLERS:
    fun onTitleTextChanged(input: String) {

        titleInputState.set(input)
    }

    fun onDescriptionTextChanged(input: String) {

        descriptionInputState.set(input)
    }

    fun onRemindMeAtTextChanged(input: Time?) {

        remindMeAtState.set(input)
    }

    fun onStartingOnTextChanged(input: Date?) {

        startingOnState.set(input)
    }

    protected fun setInputStateErrors() {

        titleInputState.setErrors()
        descriptionInputState.setErrors()
        remindMeAtState.setErrors()

        if (mTodoType.value == TodoType.WEEKLY) {
            startingOnState.setErrors()
        }
    }

    protected fun isInputValid(): Boolean {

        return titleInputState.isValid() && descriptionInputState.isValid() && remindMeAtState.isValid()
                && (startingOnState.isValid() || mTodoType.value == TodoType.DAILY)
    }


    // UTILITY:
    fun onShowTimePickerComplete() {

        mShowTimePicker.value = false
    }

    fun onShowDatePickerComplete() {

        mShowDatePicker.value = false
    }

    // CLICK HANDLERS:
    fun onTodoTypeChanged(type: TodoType) {

        mTodoType.value = type
    }

    fun onTodoColorChanged(color: ToDoColor) {

        mTodoColor.value = color
    }

    fun onRemindMeAtClicked() {

        mShowTimePicker.value = true
    }

    fun onStartingOnClicked() {

        mShowDatePicker.value = true
    }
}