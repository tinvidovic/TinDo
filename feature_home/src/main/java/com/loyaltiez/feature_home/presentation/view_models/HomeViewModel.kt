package com.loyaltiez.feature_home.presentation.view_models

import android.app.Application
import androidx.lifecycle.*
import com.loyaltiez.core.domain.ToDo

class HomeViewModel(mApplication: Application) :
    AndroidViewModel(mApplication) {

    // NAVIGATION:
    private val mNavigateToCreateToDo = MutableLiveData(false)
    val navigateToCreateToDo: LiveData<Boolean>
        get() = mNavigateToCreateToDo

    private val mNavigateToEditToDo = MutableLiveData<ToDo?>(null)
    val navigateToEditToDo: LiveData<ToDo?>
        get() = mNavigateToEditToDo

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

    // CLICK HANDLERS:

    fun onCreateToDoClicked() {

        mNavigateToCreateToDo.value = true
    }

    fun onEditTindoClicked(tindo: ToDo) {

        mNavigateToEditToDo.value = tindo
    }
}