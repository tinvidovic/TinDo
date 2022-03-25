package com.loyaltiez.core.presentation.fragments

import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout

/*
Defines a Tindo application fragment, which all other fragments inherit from and
defines some convenience functions
 */
open class TinDoFragment : Fragment() {

    // Sets an observer for the specified navigation flag, and triggers navigation
    // When the flag is set. Finally calls the onNavigationComplete function to reset the flag
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

    // Sets an observer for the error livedata field and when it is triggered
    // sets an error on the specified textInputLayout
    // The function also takes a ScrollView parameter and scrolls to the bottom of the textInputLayout
    // on errors
    fun observeTextInputError(
        error: LiveData<Int?>,
        textInputLayout: TextInputLayout,
        scrollView: ScrollView,
        errorString: () -> String?,
    ) {
        error.observe(
            viewLifecycleOwner
        ) {
            textInputLayout.error =
                errorString()

            if (it != null) {

                scrollView.post {
                    scrollView.scrollTo(
                        0,
                        textInputLayout.bottom
                    )
                }
            }
        }
    }
}