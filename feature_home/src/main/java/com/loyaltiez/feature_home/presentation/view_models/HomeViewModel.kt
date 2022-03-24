package com.loyaltiez.feature_home.presentation.view_models

import android.app.Application
import androidx.lifecycle.*
import com.loyaltiez.core.TindoApplication
import com.loyaltiez.core.domain.model.todo.DailyToDo
import com.loyaltiez.core.domain.model.todo.ToDo
import com.loyaltiez.core.domain.repository.IToDoDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeViewModel(val mApplication: Application, val toDoDAO: IToDoDAO) :
    AndroidViewModel(mApplication) {

    // NAVIGATION:
    private val mNavigateToCreateToDo = MutableLiveData(false)
    val navigateToCreateToDo: LiveData<Boolean>
        get() = mNavigateToCreateToDo

    private val mNavigateToEditToDo = MutableLiveData<ToDo?>(null)
    val navigateToEditToDo: LiveData<ToDo?>
        get() = mNavigateToEditToDo

    // TO DO LIST:
    private val mToDos = MutableLiveData<MutableList<ToDo>>(mutableListOf())
    val toDos: LiveData<MutableList<ToDo>>
        get() = mToDos

    init {

        getDailyToDos()

    }

    class Factory(application: Application, toDoDAO: IToDoDAO) : ViewModelProvider.Factory {

        // Get the application
        private val mApplication = application
        private val mToDoDAO = toDoDAO

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {

                // We have already checked the casting compatibility
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(mApplication, mToDoDAO) as T

            } else {

                throw IllegalArgumentException("Unable to construct HomeViewModel")

            }
        }
    }

    // NAVIGATION:
    fun onNavigateToCreateToDoComplete() {

        mNavigateToCreateToDo.value = false
    }

    fun onNavigateToEditToDoComplete() {

        mNavigateToEditToDo.value = null
    }

    // METHODS:
    fun getDailyToDos() : Flow<List<ToDo>> = toDoDAO.getToDosForUserEmail( (mApplication as TindoApplication).loggedInUser!!.email )

    fun deleteToDo(todo : ToDo) {

        viewModelScope.launch {

            toDoDAO.deleteToDo(todo)
        }
    }

    // CLICK HANDLERS:

    fun onCreateToDoClicked() {

        mNavigateToCreateToDo.value = true
    }

    fun onEditTindoClicked(tindo: ToDo) {

        mNavigateToEditToDo.value = tindo
    }

    fun onDeleteTindoClicked(tindo: ToDo) {

        deleteToDo(tindo)
    }
}