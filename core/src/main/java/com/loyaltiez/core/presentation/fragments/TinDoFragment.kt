package com.loyaltiez.core.presentation.fragments

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout

open class TinDoFragment : Fragment() {

    fun observeFragmentToActivityNavigationFlag(
        navigationFlag: LiveData<Boolean>,
        directions: () -> NavDirections,
        onNavigationComplete: () -> Unit,
        popCurrentActivityFromBackstack: Boolean = false
    ) {

        navigationFlag.observe(
            viewLifecycleOwner
        ) {
            if (it) {

                val navController = findNavController()

                navController.navigate(
                    directions()
                )

                onNavigationComplete()

                // If the current activity should be popped from the backstack (e.g. SplashScreen)
                if (popCurrentActivityFromBackstack) {

                    requireActivity().finishAffinity()
                    requireActivity().finish()
                }
            }
        }
    }

    fun observeTextInputError(
        error: LiveData<Int?>,
        textInputLayout: TextInputLayout,
        errorString: () -> String?,
    ) {
        error.observe(
            viewLifecycleOwner
        ) {
            textInputLayout.error =
                errorString()
        }
    }
}