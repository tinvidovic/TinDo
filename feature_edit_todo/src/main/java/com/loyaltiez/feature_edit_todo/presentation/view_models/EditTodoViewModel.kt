package com.loyaltiez.feature_edit_todo.presentation.view_models

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.loyaltiez.core.domain.ToDo
import com.loyaltiez.core.domain.WeeklyToDo
import com.loyaltiez.create_edit_todo_core.domain.TodoType
import com.loyaltiez.create_edit_todo_core.domain.getToDoColorFromColorValue
import com.loyaltiez.create_edit_todo_core.presentation.view_models.CreateEditTodoViewModel

class EditTodoViewModel(mApplication: Application, mTodo: ToDo) : CreateEditTodoViewModel(mApplication) {


    init {

        onTodoColorChanged(getToDoColorFromColorValue(mTodo.color))
        onTitleTextChanged(mTodo.title)
        onDescriptionTextChanged(mTodo.description)

        onRemindMeAtTextChanged(mTodo.time)

        if (mTodo is WeeklyToDo){

            onTodoTypeChanged(TodoType.WEEKLY)
            onStartingOnTextChanged(mTodo.getDate())
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

    // CLICK HANDLERS:
    fun onSaveClicked() {

        if (isInputValid()){
            mNavigateToHome.value = true
        } else {

            setInputStateErrors()
        }
    }
}