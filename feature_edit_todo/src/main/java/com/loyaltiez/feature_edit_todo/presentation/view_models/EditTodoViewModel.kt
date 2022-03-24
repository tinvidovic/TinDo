package com.loyaltiez.feature_edit_todo.presentation.view_models

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.loyaltiez.core.TindoApplication
import com.loyaltiez.core.domain.model.todo.DailyToDo
import com.loyaltiez.core.domain.model.todo.ToDo
import com.loyaltiez.core.domain.model.todo.WeeklyToDo
import com.loyaltiez.core.domain.repository.IToDoDAO
import com.loyaltiez.create_edit_todo_core.domain.TodoType
import com.loyaltiez.create_edit_todo_core.domain.getToDoColorFromColorValue
import com.loyaltiez.create_edit_todo_core.presentation.view_models.CreateEditTodoViewModel
import kotlinx.coroutines.launch

class EditTodoViewModel(val mApplication: Application, val mTodo: ToDo, val toDoDAO: IToDoDAO) : CreateEditTodoViewModel(mApplication) {


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

    class Factory(application: Application, todo: ToDo, toDoDAO: IToDoDAO) : ViewModelProvider.Factory {

        // Get the application and the todoObject
        private val mApplication = application
        private val mTodo = todo
        private val mToDoDAO = toDoDAO

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            if (modelClass.isAssignableFrom(EditTodoViewModel::class.java)) {

                // We have already checked the casting compatibility
                @Suppress("UNCHECKED_CAST")
                return EditTodoViewModel(mApplication, mTodo, mToDoDAO) as T

            } else {

                throw IllegalArgumentException("Unable to construct EditTodoViewModel")

            }
        }
    }

    private fun createTodo(toDo: ToDo){

        viewModelScope.launch {

            toDoDAO.insertToDo(toDo)
            mNavigateToHome.value = true
        }
    }

    // CLICK HANDLERS:
    fun onSaveClicked() {

        if (isInputValid()){

            if (mTodoType.value == TodoType.DAILY){

                val dailyToDo = DailyToDo( (mApplication as TindoApplication).loggedInUser!!.email, titleInputState.formattedValue.value!!,
                    descriptionInputState.formattedValue.value!!, mTodoColor.value!!.color, remindMeAtState.input.value!!, mTodo.id )
                createTodo(dailyToDo)
            } else if (mTodoType.value == TodoType.WEEKLY){
                val weeklyToDo = WeeklyToDo( (mApplication as TindoApplication).loggedInUser!!.email, titleInputState.formattedValue.value!!,
                    descriptionInputState.formattedValue.value!!, mTodoColor.value!!.color, remindMeAtState.input.value!!, startingOnState.input.value!!, mTodo.id )
                createTodo(weeklyToDo)
            }

            mNavigateToHome.value = true
        } else {

            setInputStateErrors()
        }
    }
}