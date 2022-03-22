package com.loyaltiez.create_edit_todo_core.presentation.view_models

import android.app.Application
import androidx.lifecycle.*
import com.loyaltiez.core.presentation.view_models.input_states.*
import com.loyaltiez.create_edit_todo_core.domain.ToDoColor
import com.loyaltiez.create_edit_todo_core.domain.TodoType
import java.sql.Date
import java.sql.Time

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
    protected val mTodoType = MutableLiveData<TodoType>(TodoType.DAILY)
    val todoType: LiveData<TodoType>
        get() = mTodoType

    // TINDO COLOR:
    protected val mTodoColor = MutableLiveData<ToDoColor>(ToDoColor.RED)
    val todoColor: LiveData<ToDoColor>
        get() = mTodoColor

    // TIME:
    val remindMeAtState = RemindMeAtState(null, mApplication.applicationContext)
    val startingOnState = StartingOnState(null, mApplication.applicationContext)

    // UTILITY:
    private val mShowTimePicker = MutableLiveData(false)
    val showTimePicker: LiveData<Boolean>
        get() = mShowTimePicker

    private val mShowDatePicker = MutableLiveData(false)
    val showDatePicker: LiveData<Boolean>
        get() = mShowDatePicker

    class Factory(application: Application) : ViewModelProvider.Factory {

        // Get the application
        private val mApplication = application

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            if (modelClass.isAssignableFrom(CreateEditTodoViewModel::class.java)) {

                // We have already checked the casting compatibility
                @Suppress("UNCHECKED_CAST")
                return CreateEditTodoViewModel(mApplication) as T

            } else {

                throw IllegalArgumentException("Unable to construct CreateEditTodoViewModel")

            }
        }
    }

    // NAVIGATION:
    fun onNavigateToHomeComplete() {

        mNavigateToHome.value = false
    }

    // INPUT HANDLERS:
    fun onTitleTextChanged(input: String){

        titleInputState.set(input)
    }

    fun onDescriptionTextChanged(input: String){

        descriptionInputState.set(input)
    }

    fun onRemindMeAtTextChanged(input : Time?){

        remindMeAtState.set(input)
    }

    fun onStartingOnTextChanged(input : Date?){

        startingOnState.set(input)
    }

    protected fun setInputStateErrors() {

        titleInputState.setErrors()
        descriptionInputState.setErrors()
        remindMeAtState.setErrors()

        if (mTodoType.value == TodoType.WEEKLY){
            startingOnState.setErrors()
        }
    }

    protected fun isInputValid(): Boolean {

        return titleInputState.isValid() && descriptionInputState.isValid() && remindMeAtState.isValid() && (startingOnState.isValid() || mTodoType.value == TodoType.DAILY)
    }

    // UTILITY:
    fun onShowTimePickerComplete(){

        mShowTimePicker.value = false
    }

    fun onShowDatePickerComplete(){

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