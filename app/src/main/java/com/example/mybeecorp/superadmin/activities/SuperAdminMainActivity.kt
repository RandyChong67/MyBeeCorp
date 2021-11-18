package com.example.mybeecorp.superadmin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.mybeecorp.activities.LoginActivity
import com.example.mybeecorp.R
import com.example.mybeecorp.navigation.superadmin.*
import com.example.mybeecorp.superadmin.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class SuperAdminMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_super_admin_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val rewardFragment = PrizesFragment_SuperAdmin()
        val insuranceFragment = InsuranceFragment_SuperAdmin()
        val usersFragment = UserFragment_SuperAdmin()
        val companyFragment = CompanyFragment_SuperAdmin()
        val vehicleFragment = VehicleFragment_SuperAdmin()
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation_superadmin)

        makeCurrentFragment(insuranceFragment)
        bottomNavigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.ic_insurance -> makeCurrentFragment(insuranceFragment)
                R.id.ic_users -> makeCurrentFragment(usersFragment)
                R.id.ic_company -> makeCurrentFragment(companyFragment)
                R.id.ic_reward -> makeCurrentFragment(rewardFragment)
                R.id.ic_vehicle -> makeCurrentFragment(vehicleFragment)
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.menu_my_profile -> {
                val intent = Intent(this, SuperAdminMyProfile::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.super_admin_main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}