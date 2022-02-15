package com.example.carcinofit.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.carcinofit.R
import com.example.carcinofit.databinding.ActivityMainBinding
import com.example.carcinofit.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.example.carcinofit.ui.camera.CameraActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {//this is lazy initialization
        DataBindingUtil.inflate(layoutInflater, R.layout.activity_main, null, false)
    }

    private val navHostFragment: NavHostFragment by lazy { // basically initializing our navigation host manager
        supportFragmentManager.findFragmentById(R.id.NavHost) as NavHostFragment // supportFragmentManager basically gives us the Fragment manager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.floatingButton.setOnClickListener { // The floating action button in the middle of bottom tab
            // Toast.makeText(applicationContext, "Floating btn Clicked", Toast.LENGTH_LONG).show()
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        val navController = navHostFragment.navController


        binding.bottomNavigationView.setupWithNavController(navController)
        navController // just setting up basic navigation
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.profileFragment, R.id.homeFragment, R.id.statisticsFragment, R.id.historyFragment -> {
                        binding.bottomNavigationView.visibility = View.VISIBLE
                        binding.bottomAppBar.visibility = View.VISIBLE // visible again cause its possible that we navigate from another activity in which case the 'else' case would have been triggerd and appbar would have been made invisible
                        binding.floatingButton.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.bottomNavigationView.visibility = View.GONE
                        binding.bottomAppBar.visibility = View.GONE
                        binding.floatingButton.visibility = View.GONE
                    }
                }
            }
    }

    override fun onNewIntent(intent: Intent?) {//D, this is related to notification , like if user finished running and our app is in foreground and then user taps on notification to see how much he ran and to see the distance he covered in map then navigate them to the tracking screen
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_TRACKING_FRAGMENT) {// D, basically we are telling intent what to do , we basically created our custom action ie. ACTION_SHOW_TRACKING_FRAGMENT
            navHostFragment.findNavController().navigate(R.id.action_global_trackingFragment)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()

    }
}