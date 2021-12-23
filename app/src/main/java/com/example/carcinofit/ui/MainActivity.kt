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

//todo
// either improve dummy using random or use firebase ML kit and it will give u the text and from that simply put all 800 chemicals in a file and and simply search in the file to match components , like first seperate them on basis of ',' and then simply one by one find them in the list

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.inflate(layoutInflater, R.layout.activity_main, null, false)
    }

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.NavHost) as NavHostFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.floatingButton.setOnClickListener {
            // Toast.makeText(applicationContext, "Floating btn Clicked", Toast.LENGTH_LONG).show()
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }


        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
        navController
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.profileFragment, R.id.homeFragment, R.id.statisticsFragment, R.id.historyFragment -> {
                        binding.bottomNavigationView.visibility = View.VISIBLE
                        binding.bottomAppBar.visibility = View.VISIBLE
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_TRACKING_FRAGMENT) {
            navHostFragment.findNavController().navigate(R.id.action_global_trackingFragment)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()

    }
}