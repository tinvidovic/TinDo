package com.loyaltiez.feature_login.presentation.view_models

import android.app.Application
import androidx.lifecycle.*
import com.loyaltiez.core.TindoApplication
import com.loyaltiez.core.domain.model.reqres.LoginResponse
import com.loyaltiez.core.domain.model.user.LoginInformation
import com.loyaltiez.core.domain.model.user.User
import com.loyaltiez.core.presentation.view_models.input_states.EmailAddressInputState
import com.loyaltiez.core.presentation.view_models.input_states.PasswordInputState
import com.loyaltiez.core.util.NetworkResource
import com.loyaltiez.feature_login.R
import com.loyaltiez.feature_login.di.LoginActivityContainer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel(val mApplication: Application) :
    AndroidViewModel(mApplication) {

    // NAVIGATION:
    private val mNavigateToHome = MutableLiveData(false)
    val navigateToHome: LiveData<Boolean>
        get() = mNavigateToHome

    // INPUT STATES:
    val emailAddressInputState = EmailAddressInputState("")
    val passwordInputState = PasswordInputState("")

    // RESPONSE:
    private val mLoginResponse = MutableLiveData<NetworkResource<LoginResponse>>()
    val loginResponse: LiveData<NetworkResource<LoginResponse>>
        get() = mLoginResponse

    // USE CASES:
    private val loginUseCase =
        ((mApplication as TindoApplication).appContainer as LoginActivityContainer).loginUseCase

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

    private fun logIn(email: String, password: String){

        viewModelScope.launch {

            loginUseCase(LoginInformation(email, password)).collect { result ->

                mLoginResponse.value = result
            }
        }
    }

    fun onLoginSuccess(networkResource: NetworkResource.Success<LoginResponse>) {

        // For now the logged in user is just injected to the AppContainer (in a production app this would have to be handled differently
        (mApplication as TindoApplication).appContainer?.loggedInUser = User(emailAddressInputState.formattedValue.value!!)

        // Navigate to the home screen
        mNavigateToHome.value = true
    }

    fun onLoginError(networkResource: NetworkResource.Error<LoginResponse>) {

        // ReqRes api has two erroneous responses (both with code 400)
        // 1. Missing password (which can never happen on our client side)
        // 2. User not found, which is handled here

        emailAddressInputState.setError(R.string.email_address_unrecognized)
    }

    // CLICK HANDLERS:
    fun onSignInClicked() {

        if (isInputValid()){
            logIn(emailAddressInputState.formattedValue.value!!, passwordInputState.formattedValue.value!!)
        } else {

            setInputStateErrors()
        }
    }
}