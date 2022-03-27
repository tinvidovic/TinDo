package com.loyaltiez.feature_home.presentation.view_models

import android.app.Application
import androidx.lifecycle.*
import com.loyaltiez.core.TindoApplication
import com.loyaltiez.core.domain.model.todo.ToDo
import com.loyaltiez.feature_home.di.HomeActivityContainer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val mApplication: Application) :
    AndroidViewModel(mApplication) {

    // NAVIGATION:
    private val mNavigateToCreateToDo = MutableLiveData(false)
    val navigateToCreateToDo: LiveData<Boolean>
        get() = mNavigateToCreateToDo

    private val mNavigateToEditToDo = MutableLiveData<ToDo?>(null)
    val navigateToEditToDo: LiveData<ToDo?>
        get() = mNavigateToEditToDo

    // TO DO LIST:
    private val mToDos = MutableLiveData<List<ToDo>>(mutableListOf())
    val toDos: LiveData<List<ToDo>>
        get() = mToDos

    // USE CASES:
    private val getToDosForUserEmailUseCase =
        ((mApplication as TindoApplication).appContainer as HomeActivityContainer).getToDosForUserEmailUseCase

    private val deleteToDoUseCase =
        ((mApplication as TindoApplication).appContainer as HomeActivityContainer).deleteToDoUseCase

    private val insertToDoUseCase =
        ((mApplication as TindoApplication).appContainer as HomeActivityContainer).insertToDoUseCase

    init {

        getToDos()

    }

    class Factory(application: Application) : ViewModelProvider.Factory {

        // Get the application
        private val mApplication = application

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {

                // We have already checked the casting compatibility
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(mApplication) as T

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
    private fun getToDos() {

        viewModelScope.launch {

            getToDosForUserEmailUseCase((mApplication as TindoApplication).loggedInUser!!.email).collect {

                mToDos.value = it
            }
        }
    }

    private fun deleteToDo(todo: ToDo) {

        viewModelScope.launch {

            deleteToDoUseCase(todo)
        }
    }

    private fun favouriteToDO(todo: ToDo) {

        viewModelScope.launch {

            // Toggle the favourite field and insert
            todo.toggleFavourite()
            insertToDoUseCase(todo)

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

    fun onFavouriteTindoClicked(tindo: ToDo) {

        favouriteToDO(tindo)
    }
}