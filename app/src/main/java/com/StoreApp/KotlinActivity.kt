package com.StoreApp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.simform.custombottomnavigation.Model
import com.simform.custombottomnavigation.SSCustomBottomNavigation
import com.StoreApp.databinding.ActivityMainBinding

/**
 * Kotlin Example for Custom Bottom Navigation
 */
class KotlinActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var binding: ActivityMainBinding


    companion object {
        private const val ID_HOME = 0
        private const val ID_EXPLORE = 1
        private const val ID_NOTIFICATION = 3
        private const val KEY_ACTIVE_INDEX = "activeIndex"
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.coler_backround_statusbar)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        // Uncomment to use the bottom navigation in traditional way, such as without using Navigation Component
        //setBottomNavigationInNormalWay(savedInstanceState)
        setBottomNavigationWithNavController(savedInstanceState)

        toolbar = findViewById(R.id.customToolbar)
        setSupportActionBar(toolbar)

        val texttoolbar:TextView = toolbar.findViewById(R.id.texttoolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        val bottomNav = findViewById<SSCustomBottomNavigation>(R.id.bottomNavigation)


        bottomNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            texttoolbar.text = destination.label ?: "Default Title"
        }



    }

    private fun setBottomNavigationWithNavController(savedInstanceState: Bundle?) {

        // If you don't pass activeIndex then by default it will take 0 position
        val activeIndex = savedInstanceState?.getInt("activeIndex") ?: ID_EXPLORE

        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val menuItems = arrayOf(


            Model(
                R.drawable.sticer,
                destinationId = R.id.navigation_home,
                id = ID_HOME,
                count = R.string.empty_value
            ),
            Model(
                R.drawable.shop,
                R.id.navigation_favorite,
                id = ID_EXPLORE,
                R.string.empty_value
            ),
            Model(
                R.drawable.buy,
                R.id.navigation_notifications,
                ID_NOTIFICATION,
                R.string.count
            ),

        )

        binding.bottomNavigation.apply {
            // If you don't pass activeIndex then by default it will take 0 position
            setMenuItems(menuItems, activeIndex)
            setupWithNavController(navController = navController, exitOnBack = false)

            // manually set the active item, so from which you can control which position item should be active when it is initialized.
            // onMenuItemClick(4)

            // If you want to change notification count
            setCount(ID_NOTIFICATION, R.string.count_update)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_ACTIVE_INDEX, binding.bottomNavigation.getSelectedIndex())
        super.onSaveInstanceState(outState)
    }
}