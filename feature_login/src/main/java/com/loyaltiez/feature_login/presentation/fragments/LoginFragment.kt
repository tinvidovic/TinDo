package com.loyaltiez.feature_login.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.loyaltiez.core.presentation.fragments.TinDoFragment
import com.loyaltiez.core.util.NetworkResource
import com.loyaltiez.feature_login.R
import com.loyaltiez.feature_login.databinding.LoginFragmentBinding
import com.loyaltiez.feature_login.presentation.view_models.LoginViewModel

class LoginFragment : TinDoFragment() {

    private val viewModel: LoginViewModel by lazy {

        // Instantiate using the Factory extension function
        ViewModelProvider(
            this,
            LoginViewModel.Factory(
                requireActivity().application
            )
        )
            .get(LoginViewModel::class.java)
    }

    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Create the binding variable
        binding = DataBindingUtil
            .inflate(
                inflater,
                R.layout.login_fragment,
                container,
                false
            )

        // Set the lifecycle owner so that live data can be observed
        binding.lifecycleOwner = viewLifecycleOwner

        // Set the viewModel for the binding variable
        binding.viewModel = viewModel

        setObservers()

        setListeners()

        return binding.root
    }

    private fun setObservers() {

        setNavigationObservers()

        setErrorObservers()

        viewModel.loginResponse.observe(
            viewLifecycleOwner
        ){
            // Distinguish between the loading, success and error states and act accordingly
            when(it){
                is NetworkResource.Loading -> {
                    binding.progressBar.show()
                    setUIEnableability(false)
                }
                is NetworkResource.Success -> {
                    binding.progressBar.hide()

                    saveUser()

                    viewModel.onLoginSuccess(it)

                }
                is NetworkResource.Error -> {
                    binding.progressBar.hide()

                    viewModel.onLoginError(it)

                    setUIEnableability(true)
                }
                else -> {
                    // Shouldn't happen, faily gracefuly
                }
            }
        }
    }

    private fun setUIEnableability(isEnabled: Boolean) {

        for (view in binding.constraintLayout.children){

            view.isEnabled = isEnabled
        }
    }

    // Saves the current users e-mail address in the sharedPreferences
    private fun saveUser() {

        activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)?.edit {
            putString(
                getString(R.string.sp_logged_in_as_key),
                viewModel.emailAddressInputState.formattedValue.value!!
            )
            apply()
        }
    }

    private fun setNavigationObservers() {

        observeFragmentToActivityNavigationFlag(
            viewModel.navigateToHome,
            { LoginFragmentDirections.actionLoginFragmentToHomeActivity()},
            viewModel::onNavigateToHomeComplete,
            true
        )
    }

    private fun setErrorObservers() {

        observeTextInputError(
            viewModel.passwordInputState.error,
            binding.outlinedTextFieldPassword,
            binding.scrollView
        ) { (viewModel.passwordInputState::getError)(requireContext()) }

        observeTextInputError(
            viewModel.emailAddressInputState.error,
            binding.outlinedTextFieldEmailAddress,
            binding.scrollView
        ) { (viewModel.emailAddressInputState::getError)(requireContext()) }

    }

    private fun setListeners() {

        setTextInputListeners()
    }

    private fun setTextInputListeners() {

        binding.outlinedTextFieldEmailAddress.editText?.doOnTextChanged { inputText, _, _, _ ->

            viewModel.onEmailAddressTextChanged(inputText.toString())
        }

        binding.outlinedTextFieldPassword.editText?.doOnTextChanged { inputText, _, _, _ ->

            viewModel.onPasswordTextChanged(inputText.toString())
        }
    }

}