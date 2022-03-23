package com.loyaltiez.feature_login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.loyaltiez.core.TindoApplication
import com.loyaltiez.feature_login.di.LoginActivityContainer

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Login flow has started. Populate LoginActivityContainer
        (application as TindoApplication).appContainer =
            LoginActivityContainer(
                (application as TindoApplication).reqResAPI,
            )

        setContentView(R.layout.activity_login)
    }

    override fun onPause() {

        // Login flow is finishing
        // Set LoginActivityContainer to null
        (application as TindoApplication).appContainer =
            null

        super.onPause()
    }

    override fun onResume() {

        // Login flow is resuming
        // Populate LoginActivityContainer
        (application as TindoApplication).appContainer =
            LoginActivityContainer(
                (application as TindoApplication).reqResAPI,
            )

        super.onResume()
    }
}