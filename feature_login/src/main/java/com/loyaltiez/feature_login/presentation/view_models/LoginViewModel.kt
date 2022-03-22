package com.loyaltiez.feature_login.presentation.view_models

import android.app.Application
import androidx.lifecycle.*
import com.loyaltiez.core.presentation.view_models.input_states.EmailAddressInputState
import com.loyaltiez.core.presentation.view_models.input_states.PasswordInputState

class LoginViewModel(mApplication: Application) :
    AndroidViewModel(mApplication) {

    // NAVIGATION:
    private val mNavigateToHome = MutableLiveData(false)
    val navigateToHome: LiveData<Boolean>
        get() = mNavigateToHome

    // INPUT STATES:
    val emailAddressInputState = EmailAddressInputState("")
    val passwordInputState = PasswordInputState("")

    class Factory(application: Application) : ViewModelProvider.Factory {

        // Get the application
        private val mApplication = application

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {

                // We have already checked the casting compatibility
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(mApplication) as T

            } else {

                throw IllegalArgumentException("Unable to construct LoginViewModel")

            }
        }
    }

    // NAVIGATION:
    fun onNavigateToHomeComplete() {

        mNavigateToHome.value = false
    }

    // INPUT HANDLERS:
    fun onEmailAddressTextChanged(input: String){

        emailAddressInputState.set(input)
    }

    fun onPasswordTextChanged(input: String){

        passwordInputState.set(input)
    }

    private fun setInputStateErrors() {

        emailAddressInputState.setErrors()
        passwordInputState.setErrors()
    }

    private fun isInputValid(): Boolean {

        return emailAddressInputState.isValid() && passwordInputState.isValid()
    }

    // CLICK HANDLERS:
    fun onSignInClicked() {

        if (isInputValid()){
            mNavigateToHome.value = true
        } else {

            setInputStateErrors()
        }
    }
}