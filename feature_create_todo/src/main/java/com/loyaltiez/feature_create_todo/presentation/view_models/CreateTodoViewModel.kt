package com.loyaltiez.feature_create_todo.presentation.view_models

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.loyaltiez.core.TindoApplication
import com.loyaltiez.core.broadcast_receivers.AlarmReceiver
import com.loyaltiez.core.domain.model.todo.DailyToDo
import com.loyaltiez.core.domain.model.todo.ToDo
import com.loyaltiez.core.domain.model.todo.WeeklyToDo
import com.loyaltiez.core.domain.repository.IToDoDAO
import com.loyaltiez.core.services.AlarmService
import com.loyaltiez.create_edit_todo_core.domain.TodoType
import com.loyaltiez.create_edit_todo_core.presentation.view_models.CreateEditTodoViewModel
import kotlinx.coroutines.launch
import java.util.*

class CreateTodoViewModel(val mApplication: Application, val toDoDAO: IToDoDAO) : CreateEditTodoViewModel(mApplication) {

    class Factory(application: Application,  toDoDAO: IToDoDAO) : ViewModelProvider.Factory {

        // Get the application
        private val mApplication = application
        private val mToDoDAO = toDoDAO

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            if (modelClass.isAssignableFrom(CreateTodoViewModel::class.java)) {

                // We have already checked the casting compatibility
                @Suppress("UNCHECKED_CAST")
                return CreateTodoViewModel(mApplication, mToDoDAO) as T

            } else {

                throw IllegalArgumentException("Unable to construct CreateTodoViewModel")

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
    fun onCreateClicked() {

        if (isInputValid()){

            if (mTodoType.value == TodoType.DAILY){

                val dailyToDo = DailyToDo( (mApplication as TindoApplication).loggedInUser!!.email, titleInputState.formattedValue.value!!,
                    descriptionInputState.formattedValue.value!!, mTodoColor.value!!.color, remindMeAtState.input.value!! )
                createTodo(dailyToDo)
            } else if (mTodoType.value == TodoType.WEEKLY){
                val weeklyToDo = WeeklyToDo( (mApplication as TindoApplication).loggedInUser!!.email, titleInputState.formattedValue.value!!,
                    descriptionInputState.formattedValue.value!!, mTodoColor.value!!.color, remindMeAtState.input.value!!, startingOnState.input.value!! )
                createTodo(weeklyToDo)
            }

        } else {

            setInputStateErrors()
        }
    }

}