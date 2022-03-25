package com.loyaltiez.feature_edit_todo.presentation.view_models

import android.app.Application
import androidx.lifecycle.*
import com.loyaltiez.core.TindoApplication
import com.loyaltiez.core.domain.model.todo.DailyToDo
import com.loyaltiez.core.domain.model.todo.ToDo
import com.loyaltiez.core.domain.model.todo.WeeklyToDo
import com.loyaltiez.core.domain.repository.IToDoDAO
import com.loyaltiez.core.domain.model.todo.TodoType
import com.loyaltiez.core.domain.model.todo.getToDoColorFromColorValue
import com.loyaltiez.create_edit_todo_core.presentation.view_models.CreateEditTodoViewModel
import kotlinx.coroutines.launch

class EditTodoViewModel(val mApplication: Application, private val mTodo: ToDo) : CreateEditTodoViewModel(mApplication) {

    // NEW TINDO:
    private val mNewTodo = MutableLiveData<ToDo?>(null)
    val newTodo: LiveData<ToDo?>
        get() = mNewTodo

    init {

        onTodoColorChanged(getToDoColorFromColorValue(mTodo.color))
        onTitleTextChanged(mTodo.title)
        onDescriptionTextChanged(mTodo.description)

        onRemindMeAtTextChanged(mTodo.time)

        if (mTodo is WeeklyToDo){

            onTodoTypeChanged(TodoType.WEEKLY)
            onStartingOnTextChanged(mTodo.date)
        } else {

            onTodoTypeChanged(TodoType.DAILY)
        }
    }

    class Factory(application: Application, todo: ToDo) : ViewModelProvider.Factory {

        // Get the application and the todoObject
        private val mApplication = application
        private val mTodo = todo

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            if (modelClass.isAssignableFrom(EditTodoViewModel::class.java)) {

                // We have already checked the casting compatibility
                @Suppress("UNCHECKED_CAST")
                return EditTodoViewModel(mApplication, mTodo) as T

            } else {

                throw IllegalArgumentException("Unable to construct EditTodoViewModel")

            }
        }
    }

    private fun createTodo(toDo: ToDo){

        viewModelScope.launch {

            insertToDoUseCase.invoke(toDo)
            mNavigateToHome.value = true
        }
    }

    // CLICK HANDLERS:
    fun onSaveClicked() {

        if (isInputValid()){

            if (mTodoType.value == TodoType.DAILY){

                // If it is a daily tindo, create it and store it in the room database
                val dailyToDo = DailyToDo( (mApplication as TindoApplication).loggedInUser!!.email, titleInputState.formattedValue.value!!,
                    descriptionInputState.formattedValue.value!!, mTodoColor.value!!.color, remindMeAtState.input.value!!, mTodo.id )
                mNewTodo.value = dailyToDo
                createTodo(dailyToDo)
            } else if (mTodoType.value == TodoType.WEEKLY){
                // If it is a weekly tindo, create it and store it in the room database
                val weeklyToDo = WeeklyToDo( (mApplication as TindoApplication).loggedInUser!!.email, titleInputState.formattedValue.value!!,
                    descriptionInputState.formattedValue.value!!, mTodoColor.value!!.color, remindMeAtState.input.value!!, startingOnState.input.value!!, mTodo.id )
                mNewTodo.value = weeklyToDo
                createTodo(weeklyToDo)
            }
        } else {

            setInputStateErrors()
        }
    }
}