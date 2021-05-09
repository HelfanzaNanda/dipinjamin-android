package com.alfardev.dipinjamin.ui.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.ui.fragments.books.BookFragment
import com.alfardev.dipinjamin.ui.fragments.carts.CartFragment
import com.alfardev.dipinjamin.ui.fragments.categories.CategoryFragment
import com.alfardev.dipinjamin.ui.fragments.home.HomeFragment
import com.alfardev.dipinjamin.ui.fragments.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        var navStatus = -1
        const val CHANNEL_ID = "dipinjamin"
        private const val CHANNEL_NAME= "dipinjamin app"
        private const val CHANNEL_DESC = "Android FCM"
    }
    private var fragment : Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        if(savedInstanceState == null){ navigation.selectedItemId = R.id.navigation_home }
        setupNotificationManager()
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.navigation_home -> {
                if(navStatus != 0){
                    fragment = HomeFragment()
                    navStatus = 0
                }
            }
            R.id.navigation_category -> {
                if(navStatus != 1){
                    fragment = CategoryFragment()
                    navStatus = 1
                }
            }

            R.id.navigation_cart -> {
                if(navStatus != 2){
                    fragment = CartFragment()
                    navStatus = 2
                }
            }
            R.id.navigation_book -> {
                if(navStatus != 3){
                    fragment = BookFragment()
                    navStatus = 3
                }
            }

            R.id.navigation_profile -> {
                if(navStatus != 4){
                    fragment = ProfileFragment()
                    navStatus = 4
                }
            }
        }
        if(fragment == null){
            navStatus = 0
            fragment = HomeFragment()
        }

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.screen_container, fragment!!)
        fragmentTransaction.commit()
        true
    }

    private fun setupNotificationManager(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_DESC
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

}