package com.loyaltiez.feature_home

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.loyaltiez.core.TindoApplication
import com.loyaltiez.core.domain.model.user.User
import com.loyaltiez.feature_home.databinding.ActivityHomeBinding
import com.loyaltiez.feature_home.databinding.HeaderNavigationDrawerBinding
import com.loyaltiez.feature_home.presentation.fragments.HomeFragmentDirections

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // The Drawer Layout
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: ActivityHomeBinding
    private lateinit var headerBinding: HeaderNavigationDrawerBinding

    private val mLoggedInUser: MutableLiveData<User> = MutableLiveData()
    val loggedInUser : LiveData<User>
        get() = mLoggedInUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mLoggedInUser.value = (application as TindoApplication).loggedInUser

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        // Set up the drawer layout and the support action bar
        drawerLayout = binding.drawerLayout

        setSupportActionBar(binding.topAppBar)

        setUpHeaderNavigationBinding()

        // first find the nav controller
        val navController = findNavController(R.id.navigation_home_host_fragment)

        // then setup the action bar, tell it about the DrawerLayout
        setupActionBarWithNavController(navController, binding.drawerLayout)

        // finally setup the left drawer
        binding.navigationView.setupWithNavController(navController)

        binding.navigationView.setNavigationItemSelectedListener(this)

    }

    /* Handle the drawer layout menu clicks
     * */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        try {
            val navController = findNavController(R.id.navigation_home_host_fragment)

            // Handle navigation view item clicks here.
            when (item.itemId) {

                R.id.logOutItem -> {
                    // User chose the "Log Out" item
                    signOut(navController)
                }
            }

            drawerLayout.close()

        }catch (e: Exception){

            // Destination can not be found from other fragments, when clicking rapidly on the drawer layout
        }

        return true
    }

    private fun signOut(navController: NavController) {

        navController.navigate(
            HomeFragmentDirections.actionHomeFragmentToLoginActivity()
        )

        getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)?.edit {
            putString(
                getString(R.string.sp_logged_in_as_key),
                null
            )
            apply()
            commit()
        }

        // Finish and remove affinity of MyBusinesses Activity
        finishAffinity()
        finish()
    }

    private fun setUpHeaderNavigationBinding() {
        headerBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.header_navigation_drawer,
            binding.navigationView,
            false
        )

        binding.navigationView.addHeaderView(headerBinding.root)

        headerBinding.viewModel = this
    }

    /**
     * Called when the hamburger menu or back button are pressed on the Toolbar
     *
     * Delegate this to Navigation.
     */
    override fun onSupportNavigateUp() = NavigationUI.navigateUp(
        findNavController(R.id.navigation_home_host_fragment),
        binding.drawerLayout
    )
}