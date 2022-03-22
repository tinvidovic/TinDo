package com.loyaltiez.feature_splash_screen.presentation.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SplashscreenViewModel(mApplication: Application) :
    AndroidViewModel(mApplication) {

    class Factory(application: Application) : ViewModelProvider.Factory {

        // Get the application
        private val mApplication = application

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            if (modelClass.isAssignableFrom(SplashscreenViewModel::class.java)) {

                // We have already checked the casting compatibility
                @Suppress("UNCHECKED_CAST")
                return SplashscreenViewModel(mApplication) as T

            } else {

                throw IllegalArgumentException("Unable to construct SplashscreenViewModel")

            }
        }
    }
}