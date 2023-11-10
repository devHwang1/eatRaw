package com.example.eatraw

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.eatraw.databinding.ActivityAdminBinding
import com.google.firebase.auth.FirebaseAuth

class AdminActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAdminBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarAdmin.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_admin)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_fish, R.id.nav_market, R.id.nav_review, R.id.nav_users,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val homeTextView = findViewById<TextView>(R.id.adminHome)
        if (homeTextView != null) {
            homeTextView.setOnClickListener {
                val homeTextView = findViewById<TextView>(R.id.adminHome)
                homeTextView.setOnClickListener {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        } else {
            // View를 찾지 못한 경우에 대한 처리
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_admin)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}