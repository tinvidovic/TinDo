package com.loyaltiez.feature_create_todo.presentation.view_models

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.loyaltiez.core.domain.DailyToDo
import com.loyaltiez.core.domain.ToDo
import com.loyaltiez.core.domain.WeeklyToDo
import com.loyaltiez.create_edit_todo_core.domain.TodoType
import com.loyaltiez.create_edit_todo_core.presentation.view_models.CreateEditTodoViewModel
import com.loyaltiez.feature_create_todo.R
import java.sql.Date
import java.sql.Time

class CreateTodoViewModel(mApplication: Application) : CreateEditTodoViewModel(mApplication) {

    class Factory(application: Application) : ViewModelProvider.Factory {

        // Get the application
        private val mApplication = application

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            if (modelClass.isAssignableFrom(CreateTodoViewModel::class.java)) {

                // We have already checked the casting compatibility
                @Suppress("UNCHECKED_CAST")
                return CreateTodoViewModel(mApplication) as T

            } else {

                throw IllegalArgumentException("Unable to construct CreateTodoViewModel")

            }
        }
    }

    // CLICK HANDLERS:
    fun onCreateClicked() {

        if (isInputValid()){
            val newTodo : ToDo

            if (mTodoType.value == TodoType.DAILY){
                newTodo = DailyToDo(titleInputState.formattedValue.value!!,
                    descriptionInputState.formattedValue.value!!, mTodoColor.value!!.color, remindMeAtState.input.value!!)
            } else {
                newTodo = WeeklyToDo(titleInputState.formattedValue.value!!,
                    descriptionInputState.formattedValue.value!!, R.color.tinDoBlue, remindMeAtState.input.value!!, startingOnState.input.value!!
                )
            }
            mNavigateToHome.value = true
        } else {

            setInputStateErrors()
        }
    }

}