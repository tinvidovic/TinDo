package com.loyaltiez.feature_splash_screen.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.loyaltiez.core.TindoApplication
import com.loyaltiez.core.animation.ViewAnimator
import com.loyaltiez.core.domain.model.user.User
import com.loyaltiez.feature_splash_screen.R
import com.loyaltiez.feature_splash_screen.databinding.SplashscreenFragmentBinding
import com.loyaltiez.feature_splash_screen.presentation.view_models.SplashscreenViewModel

// Suppressing the warning here, there is a better approach now, but I used this for demo purposes
@SuppressLint("CustomSplashScreen")
class SplashscreenFragment : Fragment() {

    private val viewModel: SplashscreenViewModel by lazy {

        // Instantiate using the Factory extension function
        ViewModelProvider(this, SplashscreenViewModel.Factory(requireActivity().application))
            .get(SplashscreenViewModel::class.java)
    }

    private lateinit var binding: SplashscreenFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Create the binding variable
        binding = DataBindingUtil
            .inflate(
                inflater,
                R.layout.splashscreen_fragment,
                container,
                false
            )

        // Set the lifecycle owner so that live data can be observed
        binding.lifecycleOwner = viewLifecycleOwner

        // Set the viewModel for the binding variable
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Animate the splash screen logo
        animate()
    }

    // Instantiates a ViewAnimator and starts a breathe navigation with the logo textView
    private fun animate() {

        val viewAnimator = ViewAnimator()

        val animatorSet = viewAnimator.breathe(
            binding.tvLogo
        )

        animatorSet.apply {
            start()
        }.doOnEnd {
            // Navigate to Login after the animation has completed
            navigate()
        }
    }

    // Navigates to LoginFragment
    private fun navigate() {

        // To prevent crashes when screen orientation changes in the middle of the process
        lifecycleScope.launchWhenResumed {
            val navController = findNavController()

            val sharedPreferences = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)

            // Get the logged in user's email address or null
            val loggedInAs = sharedPreferences?.getString(
                getString(R.string.sp_logged_in_as_key),
                null
            )

            if (loggedInAs != null){
                // For now the logged in user is just injected to the AppContainer (in a production app this would have to be handled differently
                (requireActivity().application as TindoApplication).loggedInUser = User(loggedInAs)

                navController.navigate(
                    SplashscreenFragmentDirections.actionSplashscreenFragmentToHomeActivity()
                )
            } else {
                navController.navigate(
                    SplashscreenFragmentDirections.actionSplashscreenFragmentToLoginActivity()
                )
            }

            // Finish the activity, and remove from back stack
            requireActivity().finishAffinity()
            requireActivity().finish()
        }
    }

}